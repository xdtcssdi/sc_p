# Generated by Django 2.0.7 on 2018-08-12 23:25

from django.conf import settings
import django.contrib.auth.models
import django.contrib.auth.validators
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('auth', '0009_alter_user_last_name_max_length'),
    ]

    operations = [
        migrations.CreateModel(
            name='UserInfo',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('password', models.CharField(max_length=128, verbose_name='password')),
                ('last_login', models.DateTimeField(blank=True, null=True, verbose_name='last login')),
                ('is_superuser', models.BooleanField(default=False, help_text='Designates that this user has all permissions without explicitly assigning them.', verbose_name='superuser status')),
                ('username', models.CharField(error_messages={'unique': 'A user with that username already exists.'}, help_text='Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.', max_length=150, unique=True, validators=[django.contrib.auth.validators.UnicodeUsernameValidator()], verbose_name='username')),
                ('first_name', models.CharField(blank=True, max_length=30, verbose_name='first name')),
                ('last_name', models.CharField(blank=True, max_length=150, verbose_name='last name')),
                ('email', models.EmailField(blank=True, max_length=254, verbose_name='email address')),
                ('is_staff', models.BooleanField(default=False, help_text='Designates whether the user can log into this admin site.', verbose_name='staff status')),
                ('is_active', models.BooleanField(default=True, help_text='Designates whether this user should be treated as active. Unselect this instead of deleting accounts.', verbose_name='active')),
                ('date_joined', models.DateTimeField(default=django.utils.timezone.now, verbose_name='date joined')),
                ('nickname', models.CharField(blank=True, max_length=50, null=True, verbose_name='昵称')),
                ('telephone', models.CharField(blank=True, max_length=11, null=True, unique=True, verbose_name='手机号码')),
                ('avatar', models.FileField(default='img/default.jpg', upload_to='%Y/%m', verbose_name='头像')),
                ('stuNo', models.CharField(max_length=20, verbose_name='学号')),
                ('token', models.CharField(max_length=200, verbose_name='融云用户token')),
                ('groups', models.ManyToManyField(blank=True, help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.', related_name='user_set', related_query_name='user', to='auth.Group', verbose_name='groups')),
                ('user_permissions', models.ManyToManyField(blank=True, help_text='Specific permissions for this user.', related_name='user_set', related_query_name='user', to='auth.Permission', verbose_name='user permissions')),
            ],
            options={
                'verbose_name': '用户信息',
                'verbose_name_plural': '用户信息',
            },
            managers=[
                ('object', django.contrib.auth.models.UserManager()),
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='Question',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('questionDescribe', models.CharField(max_length=500, verbose_name='问题描述')),
                ('questionType', models.IntegerField(choices=[(0, '单选'), (1, '判断'), (2, '多选')], verbose_name='问题类型')),
                ('limitedTime', models.IntegerField(default=60, verbose_name='限时（秒）')),
            ],
            options={
                'verbose_name': '问题表',
                'verbose_name_plural': '问题表',
            },
        ),
        migrations.CreateModel(
            name='QuestionOption',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('questionOptionDescribe', models.CharField(max_length=500, verbose_name='选项')),
                ('questionOptionAnswer', models.BooleanField(verbose_name='正确')),
            ],
            options={
                'verbose_name': '问题选项表',
                'verbose_name_plural': '问题选项表',
            },
        ),
        migrations.CreateModel(
            name='SignIn',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('sign_time', models.IntegerField(verbose_name='签到时间')),
                ('sign_date', models.DateField(auto_now_add=True, verbose_name='时间')),
                ('is_sign', models.BooleanField(default=False, verbose_name='签到成功')),
                ('student', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL, verbose_name='学生')),
            ],
            options={
                'verbose_name': '签到表',
                'verbose_name_plural': '签到表',
            },
        ),
        migrations.CreateModel(
            name='StuAndQue',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('detail', models.IntegerField(choices=[(0, '错误'), (1, '正确'), (2, '还没有回答')], default=2, verbose_name='回答情况')),
                ('question', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.Question', verbose_name='问题')),
                ('student', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to=settings.AUTH_USER_MODEL, verbose_name='学生')),
            ],
            options={
                'verbose_name': '学生回答的问题表',
                'verbose_name_plural': '学生回答的问题表',
            },
        ),
        migrations.CreateModel(
            name='TeachingClass',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('weekTh', models.CharField(choices=[('Monday', '星期一'), ('Tuesday', '星期二'), ('Wednesday', '星期三'), ('Thursday', '星期四'), ('Friday', '星期五'), ('Saturday', '星期六'), ('Sunday', '星期天')], default='Monday', max_length=20, verbose_name='周次')),
                ('classStartTime', models.TimeField(verbose_name='上课时间')),
                ('classEndTime', models.TimeField(verbose_name='下课时间')),
                ('classSignTime', models.IntegerField(verbose_name='学生签到需要时间')),
                ('className', models.CharField(max_length=100, verbose_name='课程名')),
                ('mac', models.CharField(max_length=100, verbose_name='路由器mac地址')),
                ('address', models.CharField(max_length=100, verbose_name='上课地址')),
                ('group_id', models.CharField(max_length=100, verbose_name='群组ID')),
                ('studentId', models.ManyToManyField(blank=True, null=True, related_name='my_class', to=settings.AUTH_USER_MODEL, verbose_name='上课的学生')),
                ('teacherId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL, verbose_name='教师工号')),
            ],
            options={
                'verbose_name': '教学班',
                'verbose_name_plural': '教学班',
            },
        ),
        migrations.CreateModel(
            name='Whitelist',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('appName', models.CharField(max_length=100, verbose_name='应用名')),
                ('packageName', models.CharField(max_length=100, verbose_name='包名')),
                ('imageResource', models.CharField(blank=True, max_length=100, null=True, verbose_name='图标')),
            ],
            options={
                'verbose_name': '白名单',
                'verbose_name_plural': '白名单',
            },
        ),
        migrations.AddField(
            model_name='signin',
            name='teaching_class',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.TeachingClass', verbose_name='教学班'),
        ),
        migrations.AddField(
            model_name='question',
            name='questionOption',
            field=models.ManyToManyField(null=True, to='api.QuestionOption', verbose_name='问题选项'),
        ),
        migrations.AddField(
            model_name='userinfo',
            name='whitelist',
            field=models.ManyToManyField(to='api.Whitelist', verbose_name='白名单应用'),
        ),
    ]
