import xadmin
from .models import Course, CourseResource, Video, Lesson


class CourseResourceAdmin(object):
    list_filter = ['name', 'course']
    list_display = ['name', 'course', 'add_time']
    search_fields = ['name', 'course']


class CourseAdmin(object):
    list_filter = ['name', 'degree', 'click_nums']
    list_display = ['name', 'degree', 'click_nums']
    search_fields = ['name', 'degree', 'click_nums']


class VideoAdmin(object):
    list_filter = ['lesson', 'name', 'add_time']
    list_display = ['lesson', 'name', 'add_time']
    search_fields = ['lesson', 'name']


class LessonAdmin(object):
    list_filter = ['name', 'course']
    list_display = ['name', 'course']
    search_fields = ['name', 'course']


xadmin.site.register(Course, CourseAdmin)
xadmin.site.register(CourseResource, CourseResourceAdmin)
xadmin.site.register(Video, VideoAdmin)
xadmin.site.register(Lesson, LessonAdmin)
