
from .models import CourseOrg, CityDict, Teacher

import xadmin


class CourseOrgAdmin:
    list_display = ['name', 'click_nums', 'fav_nums', 'address', 'add_time']
    list_fields = ['name', 'click_nums', 'fav_nums', 'address']
    search_fields = ['name', 'click_nums', 'fav_nums', 'address']


class CityDictAdmin:
    list_display = ['name']
    list_fields = ['name']
    search_fields = ['name']


class TeacherAdmin:
    list_display = ['name', 'org', 'click_nums', 'fav_nums', 'add_time']
    list_fields = ['name', 'org', 'click_nums', 'fav_nums']
    search_fields = ['name', 'org', 'click_nums', 'fav_nums']


xadmin.site.register(CourseOrg, CourseOrgAdmin)
xadmin.site.register(CityDict, CityDictAdmin)
xadmin.site.register(Teacher, TeacherAdmin)
