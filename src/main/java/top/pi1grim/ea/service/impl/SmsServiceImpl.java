package top.pi1grim.ea.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.pi1grim.ea.service.SmsService;


@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    public void sendShortMessage(String stu, String status, String phone) {
        try {
            Credential cred = new Credential(System.getenv("SMS_SECRET_ID"), System.getenv("SMS_SECRET_KEY"));
            SmsClient client = new SmsClient(cred, "ap-guangzhou");
            SendSmsRequest req = new SendSmsRequest();

            req.setSmsSdkAppId("1400808121");
            req.setSignName("PI1GRIM网");
            req.setTemplateId("1760366");


            String[] templateArgs = {stu, status};
            req.setTemplateParamSet(templateArgs);
            String[] numberList = {"+86" + phone};
            req.setPhoneNumberSet(numberList);

            SendSmsResponse res = client.SendSms(req);
            log.info(SendSmsResponse.toJsonString(res));

        } catch (Exception e) {
            log.error("interrupt occur", e);
        }
    }

}
