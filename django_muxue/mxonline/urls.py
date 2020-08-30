from django.conf.urls import url, include
import xadmin
from django.views.generic import TemplateView
from apps.users.views import LoginView, RegisterView, ModifyPWDView , ResetView , ActiveUserView, ForgetPWD

from django.views.static import serve
from mxonline.settings import MEDIA_ROOT
urlpatterns = [
    url(r'^xadmin/', xadmin.site.urls),
    url(r'^$', TemplateView.as_view(template_name='index.html'), name='index'),
    url(r'^login/$', LoginView.as_view(), name='login'),
    url(r'^register/$', RegisterView.as_view(), name='register'),
    url(r'^captcha/', include('captcha.urls')),
    url(r'^active/(?P<active_code>.*)/$', ActiveUserView.as_view(), name='user_active'),
    url(r'^reset/(?P<active_code>.*)/$', ResetView.as_view(), name='reset_pwd'),
    url(r'^forget/$', ForgetPWD.as_view(), name='forget_pwd'),
    url(r'^modifypwd/$', ModifyPWDView.as_view(), name='modify_pwd'),
    url(r'^org/',include('organization.urls', namespace='org')),
    url(r'^media/(?P<path>.*)$', serve, {'document_root': MEDIA_ROOT}),
]
