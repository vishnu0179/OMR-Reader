import cv2
import numpy as np
from . import utils
from .warp import warpImage
from . import qpart2

import json

def evaluateAnswerKey(img):
    widthImg = 700
    heightImg = 500

    # img = cv2.imread(path)

    # cropping the image assuming the marksheet is at top
    img = img[10:600, 10:800]

    # Canny Conversion of Image
    img = cv2.resize(img, (widthImg,heightImg))
    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5,5), 1)
    imgCanny = cv2.Canny(imgBlur, 10, 50)

    contours, hierarchy = cv2.findContours(imgCanny, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # Finding Rectangular Boxes and their vertices
    rectContours = utils.rectContours(contours)

    q_part1_contour_pts = utils.getCornerPoints(rectContours[2])
    q_part2_contour_pts = utils.getCornerPoints(rectContours[1])

    q_part1_contour_pts = utils.reorder(q_part1_contour_pts)

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

    result = json.dumps(responseDict)

    return result

