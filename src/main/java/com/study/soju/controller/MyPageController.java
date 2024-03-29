package com.study.soju.controller;

import com.study.soju.entity.*;
import com.study.soju.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    MyPageService myPageService;

    @Autowired
    SignUpService signUpService;

    @Autowired
    PayService payService;

    @Autowired
    RecruitStudyService recruitStudyService;

    @Autowired
    RecruitMentorService recruitMentorService;

    @Autowired
    RecruitMenteeService recruitMenteeService;

    //마이페이지 메인
    @GetMapping("")
    public String myPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/n";
        } else {
            //닉네임 검색
            String nickname = signUpService.returnNickname(principal.getName());
            //모델 바인딩
            model.addAttribute("nickname", nickname);
            //알림 카운트 검색
            int alarmCount = myPageService.alarmCount(principal.getName());
            //모델 바인딩
            model.addAttribute("alarmCount", alarmCount);
            //본인에게 온 알림 리스트 검색
            List<Alarm> alarmList = myPageService.findEmailId(principal.getName());
            //모델 바인딩
            model.addAttribute("alarmList", alarmList);
            //멤버 객체 검색
            Member member = myPageService.returnMember(principal.getName());
            //모델 바인딩
            model.addAttribute("member", member);
            return "MyPage/MyPage";
        }
    }

    //알림 페이지
    @GetMapping("/alarm")
    public String alarm(Principal principal, Model model) {
        //본인에게 온 알림 리스트 검색
        List<Alarm> alarmList = myPageService.findEmailId(principal.getName());
        //모델 바인딩
        model.addAttribute("list", alarmList);
        return "MyPage/Alarm";
    }

    //알림에서 확인 버튼을 눌렀을때
    @GetMapping("/alarm/accept")
    @ResponseBody
    public String alarmAccept(Alarm alarm, Principal principal) {
        //결과값을 다시 보내주기위한 결과값 변수
        String result = "";
        //신청한 사람의 이메일 검색
        String clientEmailId = myPageService.returnEmailId(alarm.getNickname());
        //서비스의 메서드를 사용하기 위해서 알림 객체, 신청한 사람의 이메일, 본인 이메일 파라미터로 전달
        int res = myPageService.accept(alarm, clientEmailId, principal.getName());
        //리턴값으로 결과값 저장
        switch(res) {
            case 1 :
                result = "study";
                break;
            case 2 :
                result = "mentor";
                break;
            case 3 :
                result = "mentee";
                break;
            case 4 :
                result = "excess";
                break;
            case 5 :
                result = "overlap";
                break;
            default :
                result = "no";
        }
        //결과값 리턴
        return result;
    }

    //알림에서 취소 버튼 눌렀을때
    @GetMapping("/alarm/refuse")
    @ResponseBody
    public String alarmRefuse(Alarm alarm) {
        //결과값을 보내주는 결과값 변수
        String res = "no";
        //서비스의 메서드를 사용하기 위해서 알림 객체를 파라미터로 전달
        res = myPageService.refuse(alarm);
        //결과값 리턴
        return res;
    }

    //알림에서 삭제 버튼을 눌렀을 때
    @GetMapping("/alarm/delete")
    @ResponseBody
    public String alarmDelete(Alarm alarm) {
        //결과값을 보내주는 결과값 변수
        String res = "no";
        //서비스의 메서드를 사용하기 위해서 알림 객체를 파라미터로 전달
        res = myPageService.delete(alarm);
        //결과값 리턴
        return res;
    }

    //결제내역 페이지
    @GetMapping("/perchaselist")
    public String perchaseList(Principal principal, Model model){
        //Principal로 유저 emailId 가져오기
        String emailId = principal.getName();
        //유저 이메일로 모든 결제 내역 가져오기
        List<Pay> findPay = payService.findPay(emailId);
        //결제내역 바인딩
        model.addAttribute("payList", findPay);
        return "MyPage/PerchaseList";
    }

    //결제 환불 버튼을 눌렀을 때
    @PostMapping("/perchaselist/refundcheck")
    @ResponseBody
    public String refundCheck(Pay pay){
        //환불 버튼을 눌렀을때 작업할 내용
        String res = "no";
        //가져온 impUid 로 환불할 결제 항목을 가져오기
        Pay resPay = payService.refundCheck(pay.getImpUid());
        if(resPay != null){
            //값이 잘 담겨있다면 내용을 환불진행중으로 바꾸기위해 업데이트
            payService.updatePay(resPay);
            //이후 yes 로 변경해서 값이 잘 변경 되었는지 알려주기
            res = "yes";
        }
        return res;
    }

    //찜 목록 페이지
    @GetMapping("/likelist")
    public String likeList(Model model, Principal principal) {
        //사용자의 idx 검색
        long idx = myPageService.returnIdx(principal.getName());
        //찜을 한 스터디 목록 검색
        List<RecruitStudy> recruitStudyList = recruitStudyService.likeList(idx);
        //모델 바인딩
        model.addAttribute("studyList", recruitStudyList);
        //찜을 한 멘토구하기 목록 검색
        List<RecruitMentor> recruitMentorList = recruitMentorService.likeList(idx);
        //모델 바인딩
        model.addAttribute("mentorList", recruitMentorList);
        //찜을 한 멘토프로필 목록 검색
        List<RecruitMentee> recruitMenteeList = recruitMenteeService.likeList(idx);
        //모델 바인딩
        model.addAttribute("menteeList", recruitMenteeList);
        return "MyPage/LikeList";
    }

    //현재 진행중인 만남 페이지
    @GetMapping("/meeting")
    public String Meeting(Principal principal, Model model) {
        //현재 진행중인만남 리스트 검색
        List<Meeting> meetingList = myPageService.meetingList(principal.getName());
        //모델 바인딩
        model.addAttribute("list", meetingList);
        return "MyPage/Meeting";
    }
}
