'''
	Title: utils.py
	Author: Zaid Sweidan
'''

import csv
from .models import PhenologyRecord

class Utils:
    ''' Utility method class '''
    
    def getRecordsFromCsv(self, n):
        ''' returns an array of n records loaded from a csv file'''
        phenology_records = []
        try:
            with open('project/assets/data.csv') as csv_file:
                csv_reader = csv.reader(csv_file, delimiter=',')
                line_count = 0
                record_index = 0
                for row in csv_reader:
                    if line_count > 0:
                        record_index += 1
                        phenology = PhenologyRecord()
                        phenology.species = row[0]
                        phenology.year = row[1]
                        phenology.julian_year = row[2]
                        phenology.plant_id = row[3]
                        phenology.num_buds = row[4]
                        phenology.num_flowers = row[5]
                        phenology.num_mature_flowers = row[6]
                        phenology.observer_initials = row[7]
                        phenology.observer_comments = row[8]
                        phenology_records.append(phenology)
                    line_count += 1
                    if line_count > n:
                        break
            csv_file.close()
            return phenology_records

        except IOError:
            print('Error: file not found or could not be read')

