from django.conf.urls import url
from django.urls import path, re_path

from . import views

urlpatterns = [
    path('login/', views.login_user),
    path('register/', views.register_user),
    path('logout/', views.logout_user),
    path('getInfo/', views.user_info),
    path('media/<str:year>/<str:month>/<str:filename>', views.my_image),
    path('get_my_class/', views.my_class),
    path('add_sign/', views.add_sign),
    path('send_group_msg/', views.send_group_msg),
    path('my_question/', views.my_question),
    path('my_white_app/', views.my_white_app),
    path('is_correct/', views.VerificationAnswer),
]