
import xadmin
from apps.users.models import EmailVerifyRecord,Banner
from xadmin import views


class BaseSetting:
    enable_themes = True
    use_bootswatch = True


class GlobalSetting:
    site_title = 'muxue admin'
    site_footer = 'muxue'
    menu_style = 'accordion'


class EmailVerifyRecordAdmin:
    list_display = ['code', 'email', 'send_type', 'send_time']
    search_fields = ['code', 'email', 'send_type', 'send_time']
    list_filter = ['code', 'email', 'send_type']


class BannerAdmin:
    list_display = ['title', 'index', 'add_time']
    search_fields = ['title', 'index']
    list_filter = ['title', 'index']


xadmin.site.register(EmailVerifyRecord, EmailVerifyRecordAdmin)
xadmin.site.register(Banner, BannerAdmin)
xadmin.site.register(views.BaseAdminView, BaseSetting)
xadmin.site.register(views.CommAdminView, GlobalSetting)
