from django.shortcuts import render, redirect, HttpResponse
from django.contrib.auth import authenticate, login
from django.contrib.auth.backends import ModelBackend
from apps.users.models import UserProfile, EmailVerifyRecord
from django.db.models import Q
from django.views.generic import View
from apps.users.forms import LoginForm, RegisterForm, ForgetForm, ModifyForm
from django.contrib.auth.hashers import make_password
from .email_send import send_register_email


class CustomBackend(ModelBackend):
    def authenticate(self, username=None, password=None, **kwargs):
        try:
            user = UserProfile.objects.get(Q(username=username) | Q(email=username))
            if user.check_password(password):
                return user

        except Exception:
            return None


class LoginView(View):

    def get(self, request):
        return render(request, 'login.html')

    def post(self, request):
        login_form = LoginForm(request.POST)
        if login_form.is_valid():
            username = request.POST.get('username', '')
            password = request.POST.get('password', '')
            user = authenticate(username=username, password=password)
            if user is not None:
                if user.is_active:
                    login(request, user)
                    return redirect('/')
                else:
                    return render(request, 'login.html', {'msg': '用户未激活'})
            else:
                msg = '用户名或密码错误'
                return render(request, 'login.html', {'msg': msg})
        else:
            return render(request, 'login.html', {'login_form': login_form})


class RegisterView(View):

    def get(self, request):
        register_form = RegisterForm()
        return render(request, 'register.html', {'register_form': register_form})

    def post(self, request):
        register_form = RegisterForm(request.POST)
        if register_form.is_valid():
            username = request.POST.get('email', '')
            if UserProfile.objects.filter(email=username):
                return render(request, 'register.html', {'register_form': register_form, 'msg': '用户已经存在'})
            password = request.POST.get('password', '')
            user_profile = UserProfile(is_staff=True,
                                       username=username,
                                       email=username,
                                       password=make_password(password),
                                       is_active=False)
            user_profile.save()
            send_register_email(username, 'register')

            return render(request, 'login.html')

        return render(request, 'register.html', {'register_form': register_form})


class ActiveUserView(View):
    def get(self, request, active_code):
        all_recodes = EmailVerifyRecord.objects.filter(code=active_code)
        if all_recodes:
            for recode in all_recodes:
                email = recode.email
                user = UserProfile.objects.get(email=email)
                user.is_active = True
                user.save()
                EmailVerifyRecord.objects.filter(code=active_code).delete()
            return redirect('login')
        else:
            return HttpResponse('<h1>用户激活码过期或不存在</h1>')

    def post(self, request):
        return redirect('login')


class ForgetPWD(View):
    def get(self, request):
        forget_form =ForgetForm()
        return render(request, 'forgetpwd.html', {'forget_form': forget_form})

    def post(self, request):
        forget_form = ForgetForm(request.POST)
        if forget_form.is_valid():
            email = request.POST.get('email', '')
            send_register_email(email, 'forget')
            return HttpResponse('<H1>邮件发送成功</H1>')
        else:
            return render(request, 'forgetpwd.html', {'forget_form': forget_form})


class ResetView(View):

    def get(self, request, active_code):
        all_recodes = EmailVerifyRecord.objects.filter(code=active_code)
        if all_recodes:
            for recode in all_recodes:
                email = recode.email
                return render(request, 'password_reset.html', {'email':email})
        else:
            return HttpResponse('<h1>用户激活码过期或不存在</h1>')


class ModifyPWDView(View):
    def post(self, request):
        modify_form = ModifyForm(request.POST)
        if modify_form.is_valid():
            pwd1 = request.POST.get('password1', '')
            pwd2 = request.POST.get('password2', '')
            email = request.POST.get('email', '')
            if pwd1 != pwd2:
                return render(request, 'password_reset.html', {'msg': '密码不一致'})
            user = UserProfile.objects.get(email=email)
            user.password = make_password(pwd1)
            user.save()
            EmailVerifyRecord.objects.filter(email=email, send_type='forget').delete()

            return render(request, 'login.html')
        else:
            email = request.POST.get('email', '')
            return render(request, 'password_reset.html',{'email': email})
