import cv2
import numpy as np

def preprocess(qPart2):

    qPart2Warp = cv2.resize(qPart2, (600, 89))
    qPart2Warp[:, 0:6] = 255
    qPart2Warp[:, -30:] = 255
    qPart2Warp[-10:,:] = 255
    qPart2Warp = qPart2Warp[25:-5,4:584]


    return qPart2Warp


# Preprocessing for contour detection
def contour_prepocessing(qPart2Warp, erode_iteration):
    imgG = cv2.cvtColor(qPart2Warp, cv2.COLOR_BGR2GRAY)
    img1 = cv2.GaussianBlur(imgG, (5,5), 1)
    imgC = cv2.threshold(img1, 242, 255, cv2.THRESH_BINARY)[1]
    kernel = np.ones((2, 2))
    imgD = cv2.dilate(imgC, kernel, iterations=1)
    imgE = cv2.erode(imgD, kernel, iterations=erode_iteration)

    return imgG, imgE