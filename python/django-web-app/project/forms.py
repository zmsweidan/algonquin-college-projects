'''
	Title: forms.py
	Author: Zaid Sweidan
'''

from django import forms

class PhenologyForm(forms.Form):
    ''' form representation for a Phenology object '''
    species = forms.CharField(label='species', max_length=100)
    year = forms.IntegerField(label='year')
    julian_year = forms.IntegerField(label='julian_year')
    plant_id = forms.IntegerField(label='plant_id')
    num_buds = forms.IntegerField(label='num_buds')
    num_flowers = forms.IntegerField(label='num_flowers')
    num_mature_flowers = forms.IntegerField(label='num_mature_flowers')
    observer_initials = forms.CharField(label='observer_initials', max_length=100)
    observer_comments = forms.CharField(label='observer_comments', max_length=400)
