from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from .models import Students, TestSet
from .user_scripts import *
import numpy as np
import urllib
import copy

from .omr_main import evaluateOMR

from .answer_key import evaluateAnswerKey

# Create your views here.
def home(request):
    
    return HttpResponse("Hello this is an omr app for hackathon")


def upload_answer_key(request):
    if request.method == 'POST':
        imgFile = request.FILES.get('file')     # File key Name 'file'
        correctMarks = request.POST.get('correct-marks')
        negativeMarks = request.POST.get('negative')

        originalImage = copy.deepcopy(imgFile)
        setId = request.POST.get('set-id')  # Key Name = 'set-id'  
        img = file_to_img(imgFile)
        answerKey = evaluateAnswerKey(img)
        test = TestSet(setId = setId, answerKey = answerKey, answerKeyImg = originalImage, correctMarks= correctMarks, negativeMarks=negativeMarks)
        test.save()
        return JsonResponse({
            'status' : 200,
            'message' : 'File Uploaded',
            'data' : answerKey
        })
    return HttpResponse("Unsuccessful")

def evaluateAnswerSheet(request):
    if request.method == "POST":
        answerSheet = request.FILES.get('file')
        setId = request.POST.get('set-id')
        

        img = copy.deepcopy(answerSheet)
        img = file_to_img(img)
        print(img.shape)
        result = evaluateOMR(img, setId)
        
        if result['status'] == 404:
            return JsonResponse(result)
        
        save_details(result['data'], answerSheet, setId)

        return JsonResponse(result)
    
def getSetDetails(request):
    if request.method == 'GET':
        setId = request.GET['set-id']
        
        student_list = getStudentList(setId)

        return JsonResponse({
            'status' : 200,
            'data' : student_list
        })
    
def checkImg(request):
    if request.method == 'POST':
        imgFile = request.FILES.get('file')
        correctMarks = request.POST.get('correct-marks')
        negativeMarks = request.POST.get('negative')
        setId = request.POST.get('set-id')
        answerKey = "check"

        test = TestSet(setId = setId, answerKey = answerKey, answerKeyImg = imgFile, correctMarks= correctMarks, negativeMarks=negativeMarks)
        test.save()
    return JsonResponse({
        'status' : 200,
        'message' : 'File Uploaded',
        'data' : answerKey
    })