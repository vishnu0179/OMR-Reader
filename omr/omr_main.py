import cv2 
import numpy as np
from . import utils
from .warp import warpImage
from . import qpart2
# path = 'Test 4.jpeg'
from .user_scripts import getAnswerKey

def evaluateOMR(img, setId):
    widthImg = 700
    heightImg = 500

    # img = cv2.imread(path)

    img = img[10:600, 10:800]   # cropping the image assuming the marksheet is at top
    print(img.shape)

    # Canny Conversion of Image
    img = cv2.resize(img, (widthImg,heightImg))
    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5,5), 1)
    imgCanny = cv2.Canny(imgBlur, 10, 50)

    # Contour Detection
    imgContour = img.copy()
    contours, hierarchy = cv2.findContours(imgCanny, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # Finding Rectangular Boxes and their vertices
    rectContours = utils.rectContours(contours)

    user_details_contour_pts = utils.getCornerPoints(rectContours[0])
    q_part1_contour_pts = utils.getCornerPoints(rectContours[2])
    q_part2_contour_pts = utils.getCornerPoints(rectContours[1])

    # Reordering pts
    user_details_contour_pts = utils.reorder(user_details_contour_pts)
    q_part1_contour_pts = utils.reorder(q_part1_contour_pts)

    # Warping the User Details Section
    user_details_warp = warpImage(img, user_details_contour_pts, widthImg, heightImg)

    # Apply Threshold
    user_details_thres = cv2.cvtColor(user_details_warp, cv2.COLOR_BGR2GRAY)
    user_details_thres = cv2.threshold(user_details_thres, 225, 255, cv2.THRESH_BINARY_INV )[1]

    # Cropping Enrollment Input
    enrollmentThres = user_details_thres[118:488, 37:417]
    enrollment_len = 10
    colList = utils.splitCols(enrollmentThres, 10, enrollment_len)

    enrollList = utils.read_omr(colList)

    # print("enrollment number",enrollList)

    # Cropping Test ID input
    test_id_thres = user_details_thres[118:478, 480:-30]
    test_id_len = 5

    colList = utils.splitCols(test_id_thres, 10, test_id_len)
    test_id_lst = utils.read_omr(colList)

    responseDict = {}   # for storing user responses

    # Ques Part 1
    qPart1Warp = warpImage(img, q_part1_contour_pts, widthImg, heightImg)
    qPart1ht, qPart1width = qPart1Warp.shape[:2]
    qPart1Warp = cv2.resize(qPart1Warp, ( qPart1width // 2 - qPart1width //5,  qPart1ht // 2))
    qPart1Warp[:, :10] = 255
    qPart1Warp[:,-25:] = 255
    qPart1Warp[:60,:] = 255
    qPart1Warp[-10:,:] = 255
    qPart1Warp = qPart1Warp[60:, :]    # Cropping ques part 1 warp

    imgG, imgE = qpart2.contour_prepocessing(qPart1Warp, 7)
    cnt, hierarchy = cv2.findContours(imgE, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    cnt = utils.filterContour(cnt)

    questionBoxes = utils.getQuestionBoxes(cnt, qPart1Warp)
    utils.solve(questionBoxes, imgG, responseDict, 0)

    # Ques Part 2
    x, y, w, h = cv2.boundingRect(rectContours[1])

    qPart2Warp = img[y:y+h, x:x+w]
    qPart2Warp = img[y:y+h, x:x+w]
    qPart2Warp = qpart2.preprocess(qPart2Warp)

    imgGray, imgEroded = qpart2.contour_prepocessing(qPart2Warp, 6)
    cnt, hierarchy = cv2.findContours(imgEroded, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    cnt = utils.filterContour(cnt)

    questionBoxes = utils.getQuestionBoxes(cnt, qPart2Warp)

    utils.solve(questionBoxes, imgGray, responseDict)

    enrollment_number = utils.list_to_string(enrollList)
    test_id = utils.list_to_string(test_id_lst)
    data = {
        "enrollment" : enrollment_number,
        "test_id" : test_id,
        "response" : responseDict
    }

    answerKey = getAnswerKey(setId)
    print(answerKey)
    if answerKey['status'] == 404:
        return answerKey
    
    answerKeyStr = answerKey['data']
    correctMarks = answerKey['correctMarks']
    negativeMarks = answerKey['negative']
    data['score'] = utils.calculateScore(answerKeyStr, responseDict, correctMarks, negativeMarks)

    result = {
        'status' : 200,
        'data' : data
    }
    return result
# cv2.imshow("img", img)
# cv2.waitKey(0)