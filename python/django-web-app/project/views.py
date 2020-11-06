'''
	Title: views.py
	Author: Zaid Sweidan
'''

from django.shortcuts import render
from django.http import HttpResponse, Http404, HttpResponseRedirect
from .models import PhenologyRecord, Label
from .forms import PhenologyForm
from .utils import Utils

# headers
headers_en = [
	'Id', 'Species', 'Year',	'Julian Day of Year', 'Plant Identification Number', 
	'Number of Buds', 'Number of Flowers', 'Number of Flowers that have Reached Maturity',
	'Observer Initials', 'Observer Comments'
]
headers_fr = ['Id', 'Espèce',	'Année',	'Jour julien', 'Numéro d’identification de la plante',
	'Nombre de bourgeons', 'Nombre de fleurs', 'Nombre de fleurs ayant atteint la maturité',
	'Initiales de l’observateur', 'Commentaires de l’observateur' ]
phenology_headings = []

i = 0
for label in headers_en:
    obj = Label()
    obj.id = i + 1
    obj.label_en = label
    obj.label_fr = headers_fr[i]
    phenology_headings.append(obj)
    i = i + 1

utils = Utils()


def index(request):
    ''' http request to load index page '''
    order_by = request.GET.get('order_by')
    if order_by == '2':
        phenology_records = PhenologyRecord.objects.order_by('species')
    elif order_by == '3':
        phenology_records = PhenologyRecord.objects.order_by('year')
    elif order_by == '4':
        phenology_records = PhenologyRecord.objects.order_by('-julian_year')
    elif order_by == '5':
        phenology_records = PhenologyRecord.objects.order_by('plant_id')
    elif order_by == '6':
        phenology_records = PhenologyRecord.objects.order_by('-num_buds')
    elif order_by == '7':
        phenology_records = PhenologyRecord.objects.order_by('-num_flowers')
    elif order_by == '8':
        phenology_records = PhenologyRecord.objects.order_by('-num_mature_flowers')
    elif order_by == '9':
        phenology_records = PhenologyRecord.objects.order_by('observer_initials')
    elif order_by == '10':
        phenology_records = PhenologyRecord.objects.order_by('observer_comments')
    else:
        phenology_records = PhenologyRecord.objects.order_by('id')
    context = {
        'phenology_headings' : phenology_headings,
        'phenology_records' : phenology_records
    }
    return render(request, 'project/index.html', context)


def reload(request):
    ''' http request for reloading data set '''
    PhenologyRecord.objects.all().delete()
    records = utils.getRecordsFromCsv(10)
    for record in records:
        record.save()
    return HttpResponseRedirect('/project/')


def detail(request, phenology_id):
    ''' http request to load detail view for adding and editing records '''
    if phenology_id == 0:  # new record (add)
        selectedRecord = PhenologyRecord()
        selectedRecord.pk = 0
    else:  # existing record (edit)
        try:
            selectedRecord = PhenologyRecord.objects.get(id=phenology_id)
            selectedRecord.pk = phenology_id
        except PhenologyRecord.DoesNotExist:
            raise Http404("Record does not exist!")
    context = {
        'phenology_headings' : phenology_headings,
        'selectedRecord': selectedRecord
    }
    return render(request, 'project/detail.html', context)


def save(request, phenology_id):
    ''' http request for adding or updating a record '''
    global phenology_records
    if request.method == 'POST':
        form = PhenologyForm(request.POST)
        savedrecord = PhenologyRecord()
        if phenology_id != 0:
            savedrecord.pk = phenology_id
        savedrecord.species = form.data['species']
        savedrecord.year = form.data['year']
        savedrecord.julian_year = form.data['julian_year']
        savedrecord.plant_id = form.data['plant_id']
        savedrecord.num_buds = form.data['num_buds']
        savedrecord.num_flowers = form.data['num_flowers']
        savedrecord.num_mature_flowers = form.data['num_mature_flowers']
        savedrecord.observer_initials = form.data['observer_initials']
        savedrecord.observer_comments = form.data['observer_comments']

        savedrecord.save()
        # if form.is_valid():
    else:
        form = PhenologyForm()
    return HttpResponseRedirect('/project/')


def delete(request, phenology_id):
    ''' http request to delete a speccific record '''
    try:
        PhenologyRecord.objects.filter(id=phenology_id).delete()
    except PhenologyRecord.DoesNotExist:
        raise Http404("Record does not exist!")
    return HttpResponseRedirect('/project/')
