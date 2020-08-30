from django.shortcuts import render, HttpResponse
from django.views.generic import View
from apps.organization.models import CourseOrg, CityDict
from pure_pagination import Paginator, PageNotAnInteger
from .forms import UserAskForm
from apps.operation.models import UserFavorate
# Create your views here.


class OrgView(View):

    def get(self, request):
        all_orgs = CourseOrg.objects.all()
        hot_orgs = all_orgs.order_by('-click_nums')[:3]

        org_count = all_orgs.count()
        all_city = CityDict.objects.all()

        city_id = request.GET.get('city', '')
        if city_id:
            all_orgs = all_orgs.filter(city_id=int(city_id))

        category = request.GET.get('ct', '')
        if category:
            all_orgs = all_orgs.filter(category=category)

        sort = request.GET.get('sort', '')
        if sort:
            if sort == 'students':
                all_orgs = all_orgs.order_by('-students')
            elif sort == 'courses':
                all_orgs = all_orgs.order_by('-course_nums')

        try:
            page = request.GET.get('page', 1)
        except PageNotAnInteger:
            page = 1

        p = Paginator(all_orgs, 6, request=request)

        orgs = p.page(page)

        return render(request, 'org-list.html', {'sort': sort, 'hot_orgs': hot_orgs, "category": category, 'city_id': city_id, 'org_list': orgs, 'city_list': all_city, 'org_count': org_count})


class AddUserAskView(View):

    def post(self, request):
        userask_form = UserAskForm(request.POST)

        if userask_form.is_valid():
            user_ask = userask_form.save(commit=True)
            return HttpResponse('{"status":"success"}', content_type='application/json')
        else:
            return HttpResponse('{"status":"fail", "msg":"添加出错"}', content_type='application/json')


class OrgHomeView(View):
    def  get(self, request, org_id):
        current_page = 'home'
        course_org = CourseOrg.objects.get(id=int(org_id))
        all_courses = course_org.course_set.all()
        all_teacher = course_org.teacher_set.all()[:1]
        return render(request, 'org-detail-homepage.html', locals())


class OrgCourseView(View):
    def  get(self, request, org_id):
        current_page = 'course'
        course_org = CourseOrg.objects.get(id=int(org_id))
        all_courses = course_org.course_set.all()
        return render(request, 'org-detail-course.html', locals())


class OrgDescView(View):
    def  get(self, request, org_id):
        current_page = 'desc'
        course_org = CourseOrg.objects.get(id=int(org_id))
        return render(request, 'org-detail-desc.html', locals())



class OrgTeacherView(View):
    def  get(self, request, org_id):
        current_page = 'teacher'
        course_org = CourseOrg.objects.get(id=int(org_id))
        all_teachers = course_org.teacher_set.all()
        return render(request, 'org-detail-teachers.html', locals())


class AddFavView(View):
    def post(self, request):
        fav_id = request.POST.get('fav_id', '0')
        fav_type = request.POST.get('fav_type', '0')

        if not request.user.is_authenticated():
            return HttpResponse('{"status":"fail", "msg":"用户未登录"}', content_type='application/json')
        exist_records = UserFavorate.objects.filter(user=request, fav_id=int(fav_id), fav_type=fav_type)
        if exist_records:
            exist_records.delete()
            return HttpResponse('{"status":"fail", "msg":"取消收藏"}', content_type='application/json')
        else:
            user_fav = UserFavorate()
            if int(fav_id) > 0 and int(fav_id) > 0:
                user_fav.fav_id = int(fav_id)
                user_fav.fav_type = int(fav_type)
                user_fav.save()
                return HttpResponse('{"status":"success", "msg":"已收藏"}', content_type='application/json')
            else:
                return HttpResponse('{"status":"fail", "msg":"收藏出错"}', content_type='application/json')