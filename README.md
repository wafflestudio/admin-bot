# admin-bot
wafflestudio team-admin-2024의 Admin Bot

## Composition

- `kanban-reporter`
  - team-admin-2024 채널에 칸반 이슈 생성

## Secrets

### Github Repository Secrets

- `NOTION_DATABASE_ID`
  - 노션의 칸반 페이지 데이터베이스 id.
- `NOTION_TOKEN`
  - 노션 칸반 페이지를 읽기 위한 권한이 있는 봇의 토큰.
- `SLACK_BOT_TOKEN`
  - 슬랙 team-admin-2024 채널에 메세지를 쏘기 위한 권한이 있는 봇의 토큰.
- `SLACK_CHANNEL_ID`
  - team-admin-2024 채널 id.
- `NOTION_ID_{NAME_INITIAL}`
  - NAME_INITIAL을 이니셜로 갖는 사람의 노션 id.
- `SLACK_ID_{NAME_INITIAL}`
  - NAME_INITIAL을 이니셜로 갖는 사람의 슬랙 멤버 id.

## Notes 

- [(kanban-reporter 노션 페이지)](https://www.notion.so/wafflestudio/3881d1ba43cd44868fef55ab4ce15ae0)

## References

- snutt-internal/snutt-team-bot [(Github)](https://github.com/wafflestudio/snutt-internal)
  - 구현 과정에 있어서 참고했습니다.
- snutt-android [(Github)](https://github.com/wafflestudio/snutt-android)
  - 구현 과정에 있어서 참고했습니다.
