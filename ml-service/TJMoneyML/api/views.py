from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import serializers
from sklearn.multioutput import MultiOutputRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import numpy as np

CATEGORIES = [
    "Еда и напитки", "Транспорт", "Жилье", "Развлечения",
    "Одежда", "Здоровье", "Образование", "Другое"
]

class CategoryExpensesSerializer(serializers.Serializer):
    month = serializers.IntegerField()
    categories = serializers.DictField(child=serializers.FloatField())

class ExpensesByCategorySerializer(serializers.Serializer):
    months = CategoryExpensesSerializer(many=True)

class PredictExpenseByCategoryView(APIView):
    def post(self, request):
        serializer = ExpensesByCategorySerializer(data=request.data)
        if serializer.is_valid():
            months = serializer.validated_data['months']
            X, y = [], []
            for i in range(len(months) - 1):
                features = [months[i]['categories'].get(cat, 0) for cat in CATEGORIES]
                targets = [months[i+1]['categories'].get(cat, 0) for cat in CATEGORIES]
                X.append(features)
                y.append(targets)
            
            if not X or not y:
                return Response({'error': 'Недостаточно данных для обучения'}, status=400)

            # Normalize the input data
            X = np.array(X)
            y = np.array(y)
            
            # Initialize and fit scalers
            X_scaler = StandardScaler()
            y_scaler = StandardScaler()
            
            X_scaled = X_scaler.fit_transform(X)
            y_scaled = y_scaler.fit_transform(y)

            # Train model on normalized data
            model = MultiOutputRegressor(RandomForestRegressor(n_estimators=100, random_state=42))
            model.fit(X_scaled, y_scaled)

            # Prepare and normalize last features for prediction
            last_features = [months[-1]['categories'].get(cat, 0) for cat in CATEGORIES]
            last_features_scaled = X_scaler.transform([last_features])

            # Make prediction and inverse transform
            predicted_scaled = model.predict(last_features_scaled)
            predicted = y_scaler.inverse_transform(predicted_scaled)[0]

            # Ensure no negative values in predictions and round to 2 decimal places
            predicted = np.maximum(predicted, 0)
            predicted = np.round(predicted, 2)

            # Create result dictionary with rounded values
            result = {cat: round(float(val), 2) for cat, val in zip(CATEGORIES, predicted)}
            total = round(float(np.sum(predicted)), 2)
            
            return Response({'predicted_expenses': result, 'total': total})
            
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
