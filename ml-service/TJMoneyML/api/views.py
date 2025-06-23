from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import serializers
from sklearn.multioutput import MultiOutputRegressor
from sklearn.ensemble import RandomForestRegressor
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
            model = MultiOutputRegressor(RandomForestRegressor(n_estimators=100, random_state=42))
            model.fit(X, y)
            last_features = [months[-1]['categories'].get(cat, 0) for cat in CATEGORIES]
            predicted = model.predict([last_features])[0]
            result = dict(zip(CATEGORIES, map(float, predicted)))
            total = float(np.sum(predicted))
            return Response({'predicted_expenses': result, 'total': total})
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
