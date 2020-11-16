from django.db import models

from cloudinary.models import CloudinaryField
# Create your models here.
class Students(models.Model):
    enrollment = models.CharField(max_length = 20)
    testId = models.CharField(max_length = 15)
    score = models.IntegerField()
    setId = models.CharField(max_length=50)
    answerKey = models.TextField()
    answerSheet = models.ImageField(upload_to='omr/')

class TestSet(models.Model):
    setId = models.CharField(max_length=50)
    answerKey = models.TextField()
    answerKeyImg = models.ImageField(upload_to='answer-key/')
    correctMarks = models.IntegerField()
    negativeMarks = models.FloatField()