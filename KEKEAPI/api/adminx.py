import xadmin

from xadmin import views
from xadmin.plugins.auth import UserAdmin

from .models import UserInfo, TeachingClass, SignIn, Question, StuAndQue, QuestionOption, Whitelist, \
    TeachingClassQuestion
from rongcloud import RongCloud


app_key = 'uwd1c0sxup3w1'
app_secret = '1ktqSF0gnca'
rcloud = RongCloud(app_key, app_secret)


class StuAndQueAdmin(object):
    list_display = ['pk', 'student', 'question', 'detail']
    search_fields = ['student', 'question']
    list_export = ('xls', 'xml', 'json')


class QuestionAdmin(object):
    list_display = ['pk', 'questionType', 'questionDescribe', 'limitedTime']
    search_fields = ['questionType', 'questionDescribe']
    list_export = ('xls', 'xml', 'json')


class WhiteAdmin(object):
    list_display = ['appName', 'packageName']
    search_fields = ['appName', 'packageName']
    list_export = ('xls', 'xml', 'json')


class SignInAdmin(object):
    list_display = ['student', 'teaching_class', 'sign_date', 'is_sign']
    search_fields = ['student', 'teaching_class']
    list_export = ('xls', 'xml', 'json')


class TeachingClassAdmin(object):
    list_export = ('xls', 'xml', 'json')
    list_display = ['className', 'week', 'teacherId', 'address', 'mac', 'group_id', 'do_action']
    search_fields = ['className', 'week', 'teacherId', 'address']

    def do_action(self, obj):
        userId_list = [obj.teacherId.username]
        for stu in obj.studentId.all():
            userId_list.append(stu.username)

        r = rcloud.Group.queryUser(groupId=obj.group_id)
        ready_list = set([item['id'] for item in r.result['users']])

        userId = ready_list - set(userId_list)
        #print(userId)
        if userId:
            rcloud.Group.quit(
                userId=list(userId),
                groupId=obj.group_id)
        userId = set(userId_list) - ready_list
        #print(userId)
        if userId:
            rcloud.Group.join(
                userId=list(userId),
                groupId=obj.group_id,
                groupName=str(obj.week + " " + obj.address + " " + obj.className + " " + str(
                    obj.teacherId.nickname) + " " + obj.mac)
            )
        return True


class UserAdmin_(UserAdmin):
    list_export = ('xls', 'xml', 'json')
    list_display = ['username', 'stuNo', 'nickname', 'telephone', 'is_staff', 'email','token']
    search_fields = ['username', 'stuNo', 'nickname', 'is_staff', 'email']


class TeachingClassQuestionAdmin(object):
    list_export = ('xls', 'xml', 'json')
    list_display = ['teachingClass', 'question']


xadmin.site.unregister(UserInfo)
xadmin.site.register(UserInfo, UserAdmin_)
xadmin.site.register(TeachingClass, TeachingClassAdmin)
xadmin.site.register(SignIn, SignInAdmin)
xadmin.site.register(QuestionOption)
xadmin.site.register(Question, QuestionAdmin)
xadmin.site.register(StuAndQue, StuAndQueAdmin)
xadmin.site.register(Whitelist, WhiteAdmin)


# 基本的修改
class BaseSetting(object):
    enable_themes = True   # 打开主题功能
    use_bootswatch = True  #


# 针对全局的
class GlobalSettings(object):
    site_title = "刻课后台管理系统"  # 系统名称
    site_footer = "刻课"      # 底部版权栏
    menu_style = "accordion"     # 将菜单栏收起来


# 注册，注意一个是BaseAdminView，一个是CommAdminView
xadmin.site.register(views.BaseAdminView, BaseSetting)
xadmin.site.register(views.CommAdminView, GlobalSettings)


xadmin.site.register(TeachingClassQuestion, TeachingClassQuestionAdmin)