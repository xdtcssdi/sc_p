# from django.contrib import admin
#
# # Register your models here.
# from django.contrib import admin
# from .models import UserInfo, TeachingClass, SignIn, Question, StuAndQue, QuestionOption, Whitelist
# from rongcloud import RongCloud
# app_key = 'uwd1c0sxup3w1'
# app_secret = '1ktqSF0gnca'
# rcloud = RongCloud(app_key, app_secret)
#
#
# class StuAndQueAdmin(admin.ModelAdmin):
#     list_display = ['pk','student', 'question', 'detail']
#
#
# class QuestionAdmin(admin.ModelAdmin):
#     list_display = ['pk', 'questionType', 'questionDescribe', 'limitedTime']
#
#
# class WhiteAdmin(admin.ModelAdmin):
#     list_display = ['appName', 'packageName']
#
#
# admin.site.register(UserInfo)
# admin.site.register(TeachingClass)
# admin.site.register(SignIn)
# admin.site.register(QuestionOption)
# admin.site.register(Question, QuestionAdmin)
# admin.site.register(StuAndQue, StuAndQueAdmin)
# admin.site.register(Whitelist, WhiteAdmin)
