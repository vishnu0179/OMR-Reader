from .models import Students, TestSet
import cv2
import numpy as np
import urllib
import json

def save_details(data, img, setId):

    enrollmentno = data['enrollment']
    test_id = data['test_id']
    score = data['score']
    answerKey = data['response']
    answerKey = json.dumps(answerKey)
    rs = Students.objects.filter(setId = setId).filter(enrollment= enrollmentno)
    if rs.exists():
        Students.objects.filter(enrollment= enrollmentno).delete()
    user = Students(enrollment = enrollmentno, testId =test_id, score = score, answerKey= answerKey,answerSheet = img,setId=setId)    
    user.save()

def getStudentList(setId):
    rs = Students.objects.filter(setId= setId)
    student_list = []
    for record in rs:
        details = {}
        details['score'] = record.score
        details['enrollment'] = record.enrollment
        details['test_id'] = record.testId
        details['answer_sheet_img'] = record.answerSheet.url
        student_list.append(details)

    return student_list    
    
def file_to_img(file):
    arr = np.asarray(bytearray(file.read()), dtype=np.uint8)
    imag = cv2.imdecode(arr, 1)
    return imag

def getAnswerKey(setId):
    
    t = TestSet.objects.filter(setId= setId)
    if t.exists():
        answerKey = t[0].answerKey
        print(type(answerKey))
        result = {
            'data' : answerKey,
            'correctMarks' : t[0].correctMarks,
            'negative': t[0].negativeMarks,
            'status': 200
        }
        return result
    
    return {
        'status' : 404,
        'message' : 'No Answer Key Found'
    }