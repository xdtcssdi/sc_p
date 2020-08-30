from django.http import HttpResponse
from xadmin.plugins.actions import BaseActionView
from rongcloud import RongCloud
import hashlib
import time

# app_key = 'uwd1c0sxup3w1'
# app_secret = '1ktqSF0gnca'
# rcloud = RongCloud(app_key, app_secret)
#
#
# class MyAction(BaseActionView):
#
#     # 这里需要填写三个属性
#     action_name = "my_action"    #: 相当于这个 Action 的唯一标示, 尽量用比较针对性的名字
#     description = (u'Test selected %(verbose_name_plural)s') #: 描述, 出现在 Action 菜单中, 可以使用 ``%(verbose_name_plural)s`` 代替 Model 的名字.
#
#     model_perm = 'change'    #: 该 Action 所需权限
#
#     # 而后实现 do_action 方法
#     def do_action(self, queryset):
#         # queryset 是包含了已经选择的数据的 queryset
#         for obj in queryset:
#             # obj 的操作
#             print(obj)
#         # 返回 HttpResponse
#         return HttpResponse()

# class JoinGroup(BaseActionView):
#     action_name = "JoinGroup"
#     description = (u'更新群组 %(verbose_name_plural)s')
#     model_perm = 'change'
#
#     def do_action(self, queryset):
#
#         for obj in queryset:
#             userId_list = [obj.teacherId.username]
#             for stu in obj.studentId.all():
#                 userId_list.append(stu.username)
#
#             r = rcloud.Group.queryUser(groupId=obj.group_id)
#             ready_list = set([item['id'] for item in r.result['users']])
#
#             userId = ready_list - set(userId_list)
#             #print(userId)
#             if userId:
#                 rcloud.Group.quit(
#                     userId=list(userId),
#                     groupId=obj.group_id)
#             userId = set(userId_list) - ready_list
#             #print(userId)
#             if userId:
#                 rcloud.Group.join(
#                     userId=list(userId),
#                     groupId=obj.group_id,
#                     groupName=str(obj.week + " " + obj.address + " " + obj.className + " " + str(
#                         obj.teacherId.nickname) + " " + obj.mac)
#                 )
#
#         return HttpResponse()