'''
	Title: models.py
	Author: Zaid Sweidan
'''

from django.db import models

class PhenologyRecord(models.Model):
    ''' model of a phenology record '''
    species = models.CharField(max_length=100)
    year = models.IntegerField(default=0)
    julian_year = models.IntegerField(default=0)
    plant_id = models.IntegerField(default=0)
    num_buds = models.IntegerField(default=0)
    num_flowers = models.IntegerField(default=0)
    num_mature_flowers = models.IntegerField(default=0)
    observer_initials = models.CharField(max_length=100)
    observer_comments = models.CharField(max_length=400)

class Label():
    id = 0
    label_fr = ''
    label_en = ''