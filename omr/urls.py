from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name="home"),
    path('upload/', views.upload_answer_key, name="answerOmr"),
    path('uploadc/', views.checkImg, name="answerOmr"),
    path('eval/', views.evaluateAnswerSheet, name="studentOmr"),
    path('students/', views.getSetDetails, name="studentList")
]   