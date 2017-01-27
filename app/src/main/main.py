__author__ = 'eric'

#!/usr/bin/python

import PIL, os, sys
from PIL import Image

directory = "drawable"

def resizeImage(ratio, folder):
    #print 'Resizing image to ', ratio, ' for destination ', folder
    newdir = './res/' + directory + '-' + folder
    print newdir
    if not os.path.exists(newdir):
        os.mkdir(newdir)
    width = int(img.size[0]*ratio)
    height = int(img.size[1]*ratio)
    imgTemp = img.resize((width, height), PIL.Image.ANTIALIAS)
    imgTemp.save(newdir + '/' + sys.argv[1])

if len(sys.argv) != 2:
    sys.exit("usage: main image.png")

newdir = './res'
if not os.path.exists(newdir):
    os.mkdir(newdir)

print 'resizing image ', sys.argv[1]
img = Image.open(sys.argv[1])

print 'base image width is ', img.size[0]
print 'base image height is ', img.size[1]

resizeImage(0.25, 'ldpi')
resizeImage(0.33, 'mdpi')
resizeImage(0.5, 'hdpi')
resizeImage(0.66, 'xhdpi')
resizeImage(1, 'xxhdpi')

