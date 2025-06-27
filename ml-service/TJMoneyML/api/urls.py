from django.urls import path
from .views import PredictExpenseByCategoryView

urlpatterns = [
    path('predict-expenses/', PredictExpenseByCategoryView.as_view(), name='predict-expenses'),
] 