'''
	Title: urls.py
	Author: Zaid Sweidan
'''

from django.urls import path
from . import views

app_name = 'project'
urlpatterns = [
    path('', views.index, name='index'),
    path('reload', views.reload, name='reload'),
    path('detail/<int:phenology_id>', views.detail, name='detail'),
    path('save/<int:phenology_id>', views.save, name='save'),
    path('delete/<int:phenology_id>', views.delete, name='delete')
]