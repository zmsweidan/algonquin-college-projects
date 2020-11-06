'''
	Title: tests.py
	Author: Zaid Sweidan
'''

from django.test import TestCase, Client
from django.http import HttpResponse
from .utils  import Utils
from .models import PhenologyRecord

class UtilsTest(TestCase):
    ''' Test case class for Utils '''

    def test_getRecordsFromCsv(self):
        ''' tests that the Utils.getRecordsFromCsv methods reads data correctly '''
        utils = Utils()
        loadedRecord = utils.getRecordsFromCsv(10)[0]
        loadedRecord.pk = 1
        testRecord = PhenologyRecord()
        testRecord.pk = 1
        testRecord.species = 'Dryas integrifolia'
        testRecord.year = 2016
        testRecord.julian_year = 169
        testRecord.plant_id = 1
        testRecord.num_buds = 12
        testRecord.num_flowers = 0
        testRecord.num_mature_flowers = 0
        testRecord.observer_initials = 'AF, IW'
        testRecord.observer_comments = ''
        self.assertEquals(loadedRecord, testRecord)
        print('...ran test_getRecordsFromCsv by Zaid Sweidan')
        
class WebTest(TestCase):
    ''' Test case class for web pages '''

    def setUp(self):
        self.client = Client()
        utils = Utils()
        if (PhenologyRecord.objects.all().count() == 0) :
            records = utils.getRecordsFromCsv(10)
            for record in records:
                record.save()

    def test_index(self):
        ''' Tests the index page'''
        response = self.client.get('/project/')
        records = response.context['phenology_records']

        # Check that the response is OK(200)
        self.assertEqual(response.status_code, 200)

        # Check that the rendered context contains 10 records
        self.assertEqual(len(records), 10)

        # Check that the records are of the right object type
        self.assertIsInstance(records[0], PhenologyRecord)

        print('...ran test_index by Zaid Sweidan')

class DbTest(TestCase):
    ''' Test case class for database '''

    def test_db(self):
        ''' Tests the database'''
        testId = 12345
        testRecord = PhenologyRecord()
        testRecord.pk = testId

        # check that record is saved in db
        testRecord.save()
        self.assertEqual(PhenologyRecord.objects.filter(id=testId).get(), testRecord)

        #check that record is deleted in db
        PhenologyRecord.objects.filter(id=testId).delete()
        self.assertFalse(PhenologyRecord.objects.filter(id=testId).exists())

        print('....ran test_db by Zaid Sweidan')