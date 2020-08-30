from django.core.mail import send_mail
from .models import EmailVerifyRecord
from random import Random
from mxonline.settings import EMAIL_FROM
from email.mime.text import MIMEText


def send_register_email(email, send_type = 'register'):
    email_record = EmailVerifyRecord()
    random_str = generate_random_str(16)
    email_record.email = email
    email_record.code = random_str
    email_record.send_type = send_type
    email_record.save()
    email_title = ''
    email_body = ''

    if send_type == 'register':
        email_title = 'muxue register link'
        email_body = 'click this link activate account' \
                     '\nhttp://127.0.0.1:8000/active/{0}'.format(random_str)

        send_status = send_mail(email_title, email_body, EMAIL_FROM, [email])
    elif send_type =='forget':
        email_title = 'muxue reset password link'
        email_body = 'click this link reset password' \
                     '\nhttp://127.0.0.1:8000/reset/{0}'.format(random_str)

        send_status = send_mail(email_title, email_body, EMAIL_FROM, [email])


def generate_random_str(length=8):
    chars = 'qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM'
    random = Random()
    m_str = ''
    for i in range(length):
        m_str += chars[random.randint(0, 51)]
    return m_str
