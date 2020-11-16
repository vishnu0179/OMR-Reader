from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from .models import Students, TestSet
import cv2
from .user_scripts import *
import numpy as np
import urllib
import copy
import json
from .omr_main import evaluateOMR

from .answer_key import evaluateAnswerKey

# Create your views here.
def home(request):
    if request.method == 'POST':
        img = request.FILES.get('file')
        img2 = copy.deepcopy(img)
        enrollmentno = '31423'
        test_id = '134352'
        score = 90
        save_details(enrollmentno, test_id, score, img)  
        arr = np.asarray(bytearray(img2.read(-1)), dtype=np.uint8)
        imag = cv2.imdecode(arr, -1)
        print(imag.shape)
        print_details()
        # url = user.answerSheet
        # req = urllib.request.urlopen(url)
        # print(imag.shape)
        print('kuch toh hua hain')
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
