import numpy as np
import cv2
from PIL import Image
import base64
import io

def main(data):
    
    # decode the data and save as cv2 image
    decoded_data = base64.b64decode(data)
    np_data = np.fromstring(decoded_data, np.uint8)
    image = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)

    # convert the CV2 image into bytes
    is_success, im_buf_arr = cv2.imencode(".jpg", kmeansSegmentation(image))
    byte_image = im_buf_arr.tobytes()

    #convert the byte to base64
    img_str = base64.b64encode(byte_image)
    return ""+str(img_str, 'utf-8')

def kmeansSegmentation(image):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    pixel_values = image.reshape((-1, 3))
    pixel_values = np.float32(pixel_values)

    #setting iteration and number of k-means
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
        # k = 24
    _, labels, (centers) = cv2.kmeans(pixel_values, 24, None, criteria, 10, cv2.KMEANS_RANDOM_CENTERS)

    centers = np.uint8(centers)
    labels = labels.flatten()

    segmented_image = centers[labels.flatten()]
    segmented_image = segmented_image.reshape(image.shape)

    return segmented_image
