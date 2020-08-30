from django.db import models
from django.contrib.auth.models import AbstractUser, UserManager
from rongcloud import RongCloud
import hashlib
import time

app_key = 'uwd1c0sxup3w1'
app_secret = '1ktqSF0gnca'
rcloud = RongCloud(app_key, app_secret)


class UserInfo(AbstractUser):
    """扩展用户信息表"""
    nickname = models.CharField(max_length=50, verbose_name='昵称', null=True, blank=True)
    telephone = models.CharField(max_length=11, blank=True, null=True, unique=True, verbose_name='手机号码')
    avatar = models.FileField(verbose_name='头像', upload_to='%Y/%m', default='img/default.jpg',null=True,blank=True)
    stuNo = models.CharField(max_length=20, verbose_name='学号')
    object = UserManager()
    token = models.CharField(max_length=200, verbose_name='融云用户token',default='',editable=False)
    whitelist = models.ManyToManyField('Whitelist', verbose_name='白名单应用',null=True,blank=True)

    def __str__(self):
        return self.username

    class Meta:
        verbose_name = '用户信息'
        verbose_name_plural = verbose_name

    def save(self, *args, **kwargs):
        super(UserInfo, self).save()
        if self.token == '':
            r = rcloud.User.getToken(
                userId=self.username,
                name='',
                portraitUri='')
            self.token = r.get().get('token', 'errorToken')

        rcloud.Message.publishPrivate(
            fromUserId='admin',
            toUserId=self.username,
            objectName='RC:TxtMsg',
            content="{\"content\":\"updateWhiteApp\"}",
            )
        super(UserInfo, self).save()




CHECKBOX_CHOICES = (
         ('Monday', '星期一'),
         ('Tuesday', '星期二'),
         ('Wednesday', '星期三'),
         ('Thursday', '星期四'),
         ('Friday', '星期五'),
         ('Saturday', '星期六'),
         ('Sunday', '星期天'),
)


class TeachingClass(models.Model):
    week = models.CharField(choices=CHECKBOX_CHOICES, verbose_name='周次', null=False,
                            blank=False, max_length=20, default='Monday')
    classStartTime = models.TimeField(verbose_name='上课时间', null=False, blank=False)
    classEndTime = models.TimeField(verbose_name='下课时间', null=False, blank=False)
    classSignTime = models.IntegerField(verbose_name='学生签到需要时间', null=False, blank=False)
    className = models.CharField(max_length=100, verbose_name='课程名', null=False, blank=False)
    teacherId = models.ForeignKey(UserInfo, verbose_name='教师名', on_delete=models.CASCADE)
    studentId = models.ManyToManyField('UserInfo', verbose_name='上课的学生', null=True, blank=True, related_name='my_class')
    mac = models.CharField(max_length=100, verbose_name='路由器mac地址')
    address = models.CharField(max_length=100, verbose_name='上课地址')
    group_id = models.CharField(max_length=100, verbose_name='群组ID',editable=False)#根据mac地址进行md5加密

    class Meta:
        verbose_name = '教学班'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.className + ' ' + self.address + ' ' + self.teacherId.username

    def save(self, force_insert=False, force_update=False, using=None,
             update_fields=None):

        if self.group_id == '':
            self.group_id = hashlib.md5(bytes(self.mac + self.week + self.address + self.className, encoding='utf8')).hexdigest()

        super(TeachingClass, self).save(force_insert=force_insert, force_update=force_update,
                                        using=using, update_fields=update_fields)


class SignIn(models.Model):
    student = models.ForeignKey(UserInfo, verbose_name='学生', null=False, blank=False, on_delete=models.CASCADE)
    teaching_class = models.ForeignKey(TeachingClass, verbose_name='教学班', null=False, blank=False,
                                       on_delete=models.CASCADE)
    sign_time = models.IntegerField(verbose_name='签到时间', null=False, blank=False)
    sign_date = models.DateField(verbose_name='时间', null=False, blank=False, auto_now_add=True)
    is_sign = models.BooleanField(verbose_name='签到成功', default=False)

    class Meta:
        verbose_name = '签到表'
        verbose_name_plural = verbose_name

    def __str__(self):
        return str(self.sign_date) + ' ' + self.teaching_class.className + ' '+self.student.username

    # def save(self, force_insert=False, force_update=False, using=None,
    #          update_fields=None):
    #     super(SignIn, self).save(force_insert=force_insert, force_update=force_update,
    #                              using=using,update_fields=update_fields)


QUESTION_TYPE = (
    (0, '单选'),
    (1, '判断'),
    (2, '多选')
)


class QuestionOption(models.Model):
    questionOptionDescribe = models.CharField(max_length=500, verbose_name='选项')
    questionOptionAnswer = models.BooleanField(verbose_name='正确')

    def __str__(self):
        return self.questionOptionDescribe + ' ' + str(self.questionOptionAnswer)

    class Meta:
        verbose_name = '问题选项表'
        verbose_name_plural = verbose_name


class Question(models.Model):
    questionDescribe = models.CharField(max_length=500, verbose_name='问题描述')
    questionType = models.IntegerField(choices=QUESTION_TYPE, verbose_name='问题类型')
    limitedTime = models.IntegerField(verbose_name='限时（秒）', default=60)
    questionOption = models.ManyToManyField(QuestionOption, verbose_name='问题选项', null=True)

    class Meta:
        verbose_name = '问题表'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.questionDescribe


ReplyDetail = ((0, '错误'), (1, '正确'), (2, '还没有回答'))


class StuAndQue(models.Model):
    student = models.ForeignKey(UserInfo, verbose_name='学生', null=True, on_delete=models.SET_NULL)
    question = models.ForeignKey(Question, verbose_name='问题', null=True, on_delete=models.SET_NULL)
    detail = models.IntegerField(choices=ReplyDetail, verbose_name='回答情况', default=2)

    class Meta:
        verbose_name = '学生回答的问题表'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.student.username + '回答了' + self.question.questionDescribe

    def save(self, force_insert=False, force_update=False, using=None,
             update_fields=None):
        super(StuAndQue, self).save()
        rcloud.Message.publishPrivate(
            fromUserId='admin',
            toUserId=self.student.username,
            objectName='RC:TxtMsg',
            content="{\"content\":\"updateQuestion\"}",
            )

    def delete(self, using=None, keep_parents=False):
        rcloud.Message.publishPrivate(
            fromUserId='admin',
            toUserId=self.student.username,
            objectName='RC:TxtMsg',
            content="{\"content\":\"updateQuestion\"}",
            )
        super(StuAndQue, self).delete(using, keep_parents)


class Whitelist(models.Model):
    appName = models.CharField(max_length=100, verbose_name='应用名')
    packageName = models.CharField(max_length=100, verbose_name='包名')
    imageResource = models.CharField(max_length=100, verbose_name='图标', blank=True, null=True)

    class Meta:
        verbose_name = '白名单'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.appName




class TeachingClassQuestion(models.Model):
    teachingClass = models.ForeignKey(TeachingClass, verbose_name='教学班', on_delete=models.CASCADE)
    question = models.ForeignKey(Question, verbose_name='问题', on_delete=models.CASCADE)

    class Meta:
        verbose_name = '班级问题'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.teachingClass.__str__() + '问题'

    def save(self, force_insert=False, force_update=False, using=None,
             update_fields=None):
        super(TeachingClassQuestion, self).save()

        s = set([stu.username for stu in self.teachingClass.studentId.all()])
        for stu in self.teachingClass.studentId.all():
            try:
                StuAndQue.objects.get(student=stu, question=self.question)
            except:
                StuAndQue.objects.create(student=stu, question=self.question)

        rcloud.Message.publishGroup(
            fromUserId='admin',
            toGroupId=s,
            objectName='RC:TxtMsg',
            content="{\"content\":\"updateQuestion\"}"
        )

