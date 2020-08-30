from django import forms
from apps.operation.models import UserAsk
import re

class UserAskForm(forms.ModelForm):
    class Meta:
        model = UserAsk
        fields = ['name', 'mobile', 'course_name']

    def clean_mobile(self):
        mobile = self.cleaned_data['mobile']
        REGEX = r'^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$'
        p = re.compile(REGEX)
        if p.match(mobile):
            return mobile
        else:
            raise forms.ValidationError(u'手机号码非法', code='invalid_mobile')