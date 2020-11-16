import cv2
import numpy as np

def warpImage(img, contour_pts, widthImg, heightImg):
    pt1 = np.float32(contour_pts)
    pt2 = np.float32([[0,0], [widthImg, 0], [0, heightImg], [widthImg, heightImg]])
    matrix = cv2.getPerspectiveTransform(pt1, pt2)

    img_warp = cv2.warpPerspective(img, matrix, (widthImg, heightImg))

    return img_warp
