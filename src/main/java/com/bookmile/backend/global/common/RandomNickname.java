package com.bookmile.backend.global.common;

import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.bookmile.backend.global.common.StatusCode.NICKNAME_TOO_MANY_REQUESTS;

@Component
@RequiredArgsConstructor
public class RandomNickname {

    private final UserRepository userRepository;

    // 형용사 배열
    private final String[] ADJECTIVES = {
            "행복한", "똑똑한", "즐거운", "강한", "빠른", "재치있는", "충성스러운", "멋진",
            "훌륭한", "아름다운", "기쁜", "사랑스러운", "환상적인", "놀라운", "매력적인", "긍정적인",
            "빛나는", "희망찬", "용감한", "따뜻한", "신나는", "친절한", "든든한", "감동적인",
            "뛰어난", "성실한", "창의적인", "자랑스러운", "유쾌한", "평화로운", "활기찬", "화려한",
            "단단한", "깨끗한", "순수한", "독창적인", "온화한", "세련된", "우아한", "강렬한",
            "현명한", "정직한", "부드러운", "밝은", "차분한", "매혹적인", "사려깊은", "겸손한",
            "도전적인", "진취적인", "낙천적인", "지혜로운", "정열적인", "기운찬", "희망적인",
            "참을성있는", "섬세한", "상냥한", "열정적인", "용감무쌍한", "평온한", "의젓한", "소박한",
            "활달한", "소심한", "냉철한", "자유로운", "독립적인", "따뜻한", "활발한", "책임감있는",
            "겸허한", "깨끗한", "친근한", "귀여운", "차가운", "유연한", "강력한", "고요한",
            "즐겁게", "집중력있는", "낙천적인", "고집센", "열린", "창의력있는", "호기심많은", "조용한",
            "단호한", "성실한", "겸손한", "활기찬", "명랑한", "평온한", "기발한", "사려깊은", "순박한", "유머있는"

    };
    // 명사 배열
    private final String[] NOUNS = {
            "사자", "호랑이", "독수리", "상어", "판다", "여우", "늑대", "용",
            "곰", "매", "강아지", "고양이", "토끼", "햄스터", "앵무새", "거북이",
            "고슴도치", "물고기", "말", "돌고래", "펭귄", "코알라", "기린", "수달",
            "코끼리", "타조", "캥거루", "표범", "치타", "수리", "부엉이", "갈매기",
            "까마귀", "독도새", "고릴라", "오랑우탄", "돼지", "양", "염소", "도마뱀",
            "독사", "알파카", "비둘기", "올빼미", "원숭이", "흰여우", "청설모", "사슴",
            "바다표범", "해마", "오소리", "미어캣", "플라밍고", "까치", "공작",
            "침팬지", "족제비", "너구리", "치와와", "참새", "북극곰", "코요테", "다람쥐",
            "가재", "문어", "불가사리", "나비", "벌", "개미", "달팽이", "청개구리",
            "호박벌", "방울뱀", "야생말", "수리부엉이", "하이에나", "바다사자", "거북",
            "참매", "푸른새", "참치", "고래상어", "백상아리", "흰수염고래", "오리", "기러기",
            "공룡", "도룡뇽", "얼룩말", "재규어", "퓨마", "붉은여우", "쥐", "참다랑어", "물범", "산양"
    };

    private final Random RANDOM = new Random();

    public String generateNickname() {
        String nickname;
        int count = 0;

        do {
            String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
            String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
            String number = String.valueOf(RANDOM.nextInt(99) + 1);

            nickname = adjective + noun + number;
            count++;
            if(count > 100) {
                throw new CustomException(NICKNAME_TOO_MANY_REQUESTS);
            }
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }
}
