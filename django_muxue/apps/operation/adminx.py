import xadmin

from .models import UserAsk,UserCourse,UserMessage,UserFavorate, CourseComments


class UserAskAdmin:
    list_display = ['name', 'course_name']
    list_filter = ['name', 'course_name']
    search_fields = ['name', 'course_name']


class UserCourseAdmin:
    list_display = ['user', 'course']
    list_filter = ['user', 'course', 'add_time']
    search_fields = ['user', 'course']


class UserMessageAdmin:
    list_display = ['user', 'has_read']
    list_filter = ['user', 'has_read']
    search_fields = ['user', 'has_read']


class UserFavorateAdmin:
    list_display = ['user', 'fav_id']
    list_filter = ['user', 'fav_id']
    search_fields = ['user', 'fav_id']


class CourseCommentsAdmin:
    list_display = ['user', 'course', 'add_time']
    list_filter = ['user', 'course']
    search_fields = ['user', 'course']


xadmin.site.register(UserAsk, UserAskAdmin)
xadmin.site.register(UserFavorate, UserFavorateAdmin)
xadmin.site.register(UserCourse, UserCourseAdmin)
xadmin.site.register(UserMessage, UserMessageAdmin)
xadmin.site.register(CourseComments, CourseCommentsAdmin)
