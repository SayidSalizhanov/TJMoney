from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import serializers
from sklearn.multioutput import MultiOutputRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import numpy as np
import warnings

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
            # Собираем данные по категориям
            category_values = {cat: [] for cat in CATEGORIES}
            for m in months:
                for cat in CATEGORIES:
                    val = m['categories'].get(cat, 0)
                    category_values[cat].append(val)
            # Клиппируем выбросы на уровне 95-го перцентиля
            for cat in CATEGORIES:
                vals = np.array(category_values[cat])
                perc95 = np.percentile(vals, 95)
                category_values[cat] = np.clip(vals, 0, perc95)
            # Формируем X и y с учётом клиппинга
            for i in range(len(months) - 1):
                features = [category_values[cat][i] for cat in CATEGORIES]
                targets = [category_values[cat][i+1] for cat in CATEGORIES]
                X.append(features)
                y.append(targets)
            # Проверяем, достаточно ли данных для обучения
            if not X or not y or len(X) < 2:
                # Если данных мало — возвращаем медиану по каждой категории
                medians = {cat: float(np.median([v for v in category_values[cat] if v > 0]) or 0) for cat in CATEGORIES}
                total = round(float(sum(medians.values())), 2)
                medians = {cat: round(val, 2) for cat, val in medians.items()}
                return Response({'predicted_expenses': medians, 'total': total})
            # Считаем, по каким категориям есть достаточно данных (не менее 2 ненулевых значений)
            valid_cats = [cat for cat in CATEGORIES if np.count_nonzero(category_values[cat]) >= 2]
            # Если ни по одной категории нет достаточно данных — fallback на медиану
            if not valid_cats:
                medians = {cat: float(np.median([v for v in category_values[cat] if v > 0]) or 0) for cat in CATEGORIES}
                total = round(float(sum(medians.values())), 2)
                medians = {cat: round(val, 2) for cat, val in medians.items()}
                return Response({'predicted_expenses': medians, 'total': total})
            # Оставляем только валидные категории для обучения
            cat_idx = [CATEGORIES.index(cat) for cat in valid_cats]
            X = np.array(X)[:, cat_idx]
            y = np.array(y)[:, cat_idx]
            # Нормализация
            X_scaler = StandardScaler()
            y_scaler = StandardScaler()
            X_scaled = X_scaler.fit_transform(X)
            y_scaled = y_scaler.fit_transform(y)
            # Обучение модели
            model = MultiOutputRegressor(RandomForestRegressor(n_estimators=100, random_state=42))
            model.fit(X_scaled, y_scaled)
            # Прогноз по последнему месяцу
            last_features = [category_values[cat][-1] for cat in valid_cats]
            last_features_scaled = X_scaler.transform([last_features])
            predicted_scaled = model.predict(last_features_scaled)
            predicted = y_scaler.inverse_transform(predicted_scaled)[0]
            # Без отрицательных значений, округление
            predicted = np.maximum(predicted, 0)
            predicted = np.round(predicted, 2)
            # Собираем результат: для невалидных категорий — медиана, для валидных — предсказание
            result = {}
            for i, cat in enumerate(CATEGORIES):
                if cat in valid_cats:
                    idx = valid_cats.index(cat)
                    result[cat] = round(float(predicted[idx]), 2)
                else:
                    vals = [v for v in category_values[cat] if v > 0]
                    result[cat] = round(float(np.median(vals) if vals else 0), 2)
            total = round(float(sum(result.values())), 2)
            return Response({'predicted_expenses': result, 'total': total})
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
