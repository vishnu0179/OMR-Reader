import cv2
import numpy as np
from functools import cmp_to_key
from .models import Students
import json


def rectContours(contours):
    rectContours = []
    for i in contours:
        area = cv2.contourArea(i)
        if area > 100:
            peri = cv2.arcLength(i, True)
            approx = cv2.approxPolyDP(i, 0.02*peri, True)
            if len(approx) == 4:
                rectContours.append(i)

    rectContours = sorted(rectContours,key= cv2.contourArea, reverse=True)
    return rectContours

def getCornerPoints(contour):
    peri = cv2.arcLength(contour, True)
    approx = cv2.approxPolyDP(contour, 0.02*peri, True)
    return approx

# def cmp(x, y):
#     # print("len", len(x[0]))
#     a = x[0][0] + x[0][1]
#     b = y[0][1] + y[0][1]
#     return abs(a + b)

def reorder(points):
    # points = points.reshape((4,2))
    # addedPoints = points.sum(1)

    # print(addedPoints)
    # points = addedPoints.reshape((4,1,2))
    points = sorted(points, key= lambda point : point[0][0] + point[0][1])
    points = np.array(points)
    if points[2][0][0] > points[1][0][0]:
        points[[2,1],:] = points[[1,2],:]
    # print(points)
    return points

def splitCols(img, nr, nc):
    # print(img.shape)
    cols = np.hsplit(img, nc)
    splittedColumn = []
    for c in cols:
        rows = np.vsplit(c, nr)
        splittedColumn.append(rows)
    # cv2.imshow('box', boxes[2] )
    
    # print(len(splittedColumn[0]))
    return splittedColumn



def filterContour(contours):
    cnt = []
    for i in contours:
        area = cv2.contourArea(i)
        # print(area) 
        if area > 800 and area < 10000:
            cnt.append(i)

    # cnt = cnt.reverse()
    cnt.reverse()
    # print(len(cnt))
    return cnt


def read_omr(colList):
    result = []
    minVal = 150   # to consider marking, if less than minVal box is considered unmarked
    for column in colList:
        val = -1
        curr_digit = -1
        for i, box in enumerate(column):
            currPixelVal = cv2.countNonZero(box)
            if currPixelVal > minVal and currPixelVal > val:
                val = currPixelVal
                curr_digit = (i+1)%10
        if curr_digit == -1:
            return []
        result.append(curr_digit)

    return result


def getNo(img):
    zommed = cv2.resize(img, (100, 150))
    kernel = np.array([[-1,-1,-1], [-1,9,-1], [-1,-1,-1]])
    zommed = cv2.filter2D(zommed, -1, kernel)
    thresh = cv2.threshold(zommed, 0, 255,cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
    kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (1, 5))
    thresh = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel)
    cnts, hier = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)
    # cv2.drawContours(zommed,cnts, -1, (0,0,255), 2)
    cv2.imshow('digit', zommed)


def comp(a, b):
    xa = a[0] // 20
    xb = b[0] // 20

    if xa < xb:
        return -1

    if xa < xb:
        return 1

    ya = a[1] // 22
    yb = b[1] //22

    if ya < yb:
        return -1
    
    if ya > yb: 
        return 1
    
    return 0


def solve(questionBox, img, responseDict, offset=10):
    # print('a',questionBox[3])
    options = []
    qlist = []

    questionBox.sort(key=cmp_to_key(comp))

    # print(questionBox)
    for x, y, w, h in questionBox:
        w = (w // 5 -1)  * 5
        # print(x,x+w)
        crop = img[y:y+h, x:x+w]
        # print(crop.shape)
        box = np.hsplit(crop, 5)
        qno = box[0]
        qlist.append(qno)
        # getNo(qno)    ----- function to get question no from image
        options.append(box[1:])
    

    selectedOption(options, offset,responseDict)


def selectedOption(options, offset, responseDict):
    
    # responseMap = []
    minVal = 2000
    for idx, ques in enumerate(options):
        # responseDict[idx+10] = []
        currQues = 0
        currQuesIdx = -1
        for i, option in enumerate(ques):
            option = cv2.resize(option, (100, 100))
            quesThres = cv2.threshold(option, 225, 255, cv2.THRESH_BINARY_INV)[1]
            currPixelVal = cv2.countNonZero(quesThres)
            if currPixelVal > minVal and currPixelVal > currQues:
                currQues = currPixelVal
                currQuesIdx = i
        if currQuesIdx != -1:
            responseDict[str(idx+offset+1)] = chr(ord("A") + currQuesIdx)
        else :
            responseDict[str(idx+offset+1)] = '-'

    # print(responseDict)
    return responseDict


def getQuestionBoxes(cnt, imgGray):

    questionBox = []
    for i in cnt:
        peri = cv2.arcLength(i, True)
        approx = cv2.approxPolyDP(i, 0.02 * peri, True)
        x, y, w, h = cv2.boundingRect(approx)
        questionBox.append((x,y,w,h))
        # cv2.rectangle(imgGray, (x, y),(x+w, y+h), (0,0,255),1)
    return questionBox

    
def list_to_string(lst):
    resString = ""
    for i in lst:
        resString += str(i)

    return resString

def calculateScore(answerKey, responseDict, correctMarks, negativeMarks = 0):
    score = 0
    answerDict = json.loads(answerKey)
    
    for key in answerDict:

        if answerDict[key] != '-':
            if responseDict[key] == answerDict[key]:
                score = score + correctMarks
            else:
                score = score - negativeMarks

    return score


