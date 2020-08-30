from datetime import time, datetime

import pytz
from django.contrib import auth
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.exceptions import ValidationError
import os

from rest_framework.utils import json

from KEKE import settings
from api.models import UserInfo, TeachingClass, SignIn, StuAndQue, Whitelist, Question
from django.core import serializers
from django.forms.models import model_to_dict
from rongcloud import RongCloud
app_key = 'uwd1c0sxup3w1'
app_secret = '1ktqSF0gnca'
rcloud = RongCloud(app_key, app_secret)

@csrf_exempt
def register_user(request):
    username = request.GET.get('username', '')
    password = request.GET.get('password', '')
    stuNo = request.GET.get('stuNo', '')

    if username == "" or password == "":
        return JsonResponse({'success': '0', 'error': '用户名或密码为空'})

    result = UserInfo.objects.filter(username=username)
    if result:
        return JsonResponse({'success': '0', 'error': '用户已经存在'})

    try:
        UserInfo.objects.create_user(username=username, password=password, stuNo=stuNo)

    except ValidationError as e:
        error = "创建失败，未知错误"
        return JsonResponse({'success': '0', 'error': error})
    return JsonResponse({'success': '1', 'error': ''})


@csrf_exempt
def login_user(request):
    username = request.GET['username']
    password = request.GET['password']
    if username == "" or password == "":
        return JsonResponse({'success': '0', 'error': '帐号或密码为空',
                             'sessionid': ''
                             })

    user = auth.authenticate(username=username, password=password)
    if user is not None:
        auth.login(request, user)
        return JsonResponse({'success': '1', 'error': '',
                             'sessionid': request.session.session_key
                             })
    return JsonResponse({'success': '0', 'error': '登录失败',
                         'sessionid': ''
                         })


@csrf_exempt
def logout_user(request):
    logout(request)
    if not request.session.session_key:
        return JsonResponse({'success': '1', 'error': ''})
    return JsonResponse({'success': '0', 'error': '注销失败'})


@csrf_exempt
def user_info(request):
    user = request.user

    return JsonResponse({'username': user.username,
                         'stuNo': user.stuNo,
                         'nickname': user.nickname,
                         'telephone': user.telephone,
                        'avatar': user.avatar.url,
                         'is_staff': user.is_staff,
                         'email': user.email,
                         'token': user.token})


@login_required
def my_image(request, year, month, filename):
    imagepath = os.path.join(settings.BASE_DIR, 'media', year, month, filename)

    image_data = open(imagepath, "rb").read()

    return HttpResponse(image_data,content_type="image/"+filename.split('.')[-1])


@login_required
def my_class(request):

    teacher_class = TeachingClass.objects.filter(
                        studentId__username=request.user.username)

    for item in teacher_class:
        item.teacherId_id = item.teacherId.nickname

    data = serializers.serialize("json", teacher_class)
    return HttpResponse("{\"data\":"+data+"}")


# now_time=23:50:00&week=Tuesday&sign_time=10
@login_required
def add_sign(request):
    user = request.user
    now_time = request.GET['now_time']
    week = request.GET['week']
    for item in TeachingClass.objects.filter(week=week).iterator():
        stus = item.studentId.all()
        flag = False
        for stu in stus:
            if stu == user:
                flag = True
        if flag:
            if (str(item.classStartTime) <= now_time) and (str(item.classEndTime) >= now_time):
                try:
                    s = SignIn.objects.get(sign_date__exact=datetime.now().date(), teaching_class=item)
                    try:
                        #参数带上sign_time 代表修改签到表的签到时间，获取签到信息
                        s.sign_time = request.GET['sign_time']
                        if int(s.sign_time) >= int(s.teaching_class.classSignTime):
                            s.is_sign = True
                        else:
                            s.is_sign = False
                        s.save()
                    except:
                        pass
                except:
                    #创建签到表
                    s = SignIn(sign_time=0, teaching_class=item, student=request.user)
                    s.save()

                return JsonResponse({
                    "success": "1",
                    "error": "",
                    "className": s.teaching_class.className,
                    "classAddress": s.teaching_class.address,
                    "teacherName": s.teaching_class.teacherId.nickname,
                    "sign_time": s.sign_time,
                    "sign_date": s.sign_date,
                    "is_sign": s.is_sign,
                    'need_sign_time': s.teaching_class.classSignTime,
                    'mac': s.teaching_class.mac
                })

    return JsonResponse({
                "success": "0",
                "error": "",
                "className": '',
                "classAddress": '',
                "teacherName": '',
                "sign_time": '',
                "sign_date": '',
                "is_sign": False,
                'need_sign_time': '',
                'mac': ''
            })


#发送信息到一个群组
@login_required
def send_group_msg(request):
    fromUserId = request.GET['fromUserId']
    toGroupId = request.GET['toGroupId']
    objectName = 'RC:TxtMsg'
    content = '{"content":"'+request.GET['content']+'"}'
    r = rcloud.Message.publishGroup(
        fromUserId=fromUserId,
        toGroupId=toGroupId,
        objectName=objectName,
        content=content)
    return HttpResponse(r)


@login_required
def my_question(request):
    user = request.user
    question = StuAndQue.objects.filter(student=user)

    datas = '{"data":['
    for idx_o,item in enumerate(question):
        data='{'+'"StuAndQuePk":' +str(item.pk)+ ',"question_pk":'+str(item.question.pk)+',' +\
        '"questionDescribe":\"'+ item.question.questionDescribe + '\",' +\
        '"questionOption":'
        tmp = ''
        options = item.question.questionOption.all()
        for idx,it in enumerate(options):
            tmp += "\""+it.questionOptionDescribe+"\""
            if idx != len(options)-1:
                tmp += ','
        data += "[" + tmp + "]," + "\"type\":" + str(item.question.questionType)
        datas += data
        datas += ',"limited":'+str(item.question.limitedTime)+"," + '"detail":'+str(item.detail) + "}"
        if idx_o != len(question)-1:
            datas += ','
    datas += ']}'

    return HttpResponse(datas)


@login_required
def my_white_app(request):
    user = request.user

    app_list = Whitelist.objects.filter(userinfo=user)

    data = '{"data":['
    for idx,app in enumerate(app_list):
        data += '["'+app.appName+'",'+'"'+app.packageName+'","'+str(app.imageResource)+'"]'
        if len(app_list)-1 != idx:
            data += ','
    data += ']}'
    print(data)
    return HttpResponse(data)


#[1,0,1,1]
@login_required
def VerificationAnswer(request):
    try:
        pk = request.GET['pk']
        option = request.GET['option']
        StuAndQuePk = request.GET['StuAndQuePk']
        lst = list(map(bool, json.loads(option)))

        q = Question.objects.get(pk=pk).questionOption.all()

        is_correct = True
        for idx, item in enumerate(q):
            if item.questionOptionAnswer != lst[idx]:
                is_correct = False
                break

        s = StuAndQue.objects.get(pk=int(StuAndQuePk))
        if is_correct:
            s.detail = 1

        else:
            s.detail = 0
        s.save()

        return HttpResponse('{"is_correct":'+ str(s.detail) +'}')
    except:
        return HttpResponse('')