# Use Case Diagram — University IAM Platform

```mermaid
flowchart LR
    SA(["SUPER_ADMIN"])
    AD(["ADMIN"])
    ST(["STUDENT"])
    TE(["TEACHER"])
    DE(["DEAN"])
    HR(["HR_MANAGER"])
    SO(["SECURITY_OFFICER"])

    subgraph IAM["University IAM Platform"]
        direction TB
        UC1(["Жүйеге кіру"])
        UC2(["Тіркелу"])
        UC3(["MFA арқылы растау"])
        UC4(["Құпиясөзді өзгерту"])
        UC5(["Жеке профильді қарау"])
        UC6(["Пайдаланушыларды басқару"])
        UC7(["Рөлдерді тағайындау"])
        UC8(["Құқықтарды басқару"])
        UC9(["Аккаунтты бұғаттау / ашу"])
        UC10(["Оқу процесі пайдаланушыларын басқару"])
        UC11(["Қызметкерлерді басқару"])
        UC12(["Қолжетімділік сұрауларын қарау"])
        UC13(["Audit Log қарау"])
        UC14(["Қауіпсіздік оқиғаларын бақылау"])
        UC15(["Қолжетімділікті тексеру"])
    end

    SA --> UC1
    SA --> UC3
    SA --> UC6
    SA --> UC7
    SA --> UC8
    SA --> UC9
    SA --> UC13
    SA --> UC15

    AD --> UC1
    AD --> UC3
    AD --> UC6
    AD --> UC7
    AD --> UC9
    AD --> UC13
    AD --> UC15

    ST --> UC1
    ST --> UC2
    ST --> UC3
    ST --> UC4
    ST --> UC5
    ST --> UC15

    TE --> UC1
    TE --> UC3
    TE --> UC4
    TE --> UC5
    TE --> UC15

    DE --> UC1
    DE --> UC3
    DE --> UC5
    DE --> UC10
    DE --> UC12
    DE --> UC15

    HR --> UC1
    HR --> UC3
    HR --> UC5
    HR --> UC11
    HR --> UC12
    HR --> UC15

    SO --> UC1
    SO --> UC3
    SO --> UC13
    SO --> UC14
    SO --> UC15
