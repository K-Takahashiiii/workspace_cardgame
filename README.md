# 🃏 カードゲーム大会運営アプリ（workspace_cardgame）

## 📘 プロジェクト概要
このリポジトリは、**カードゲーム大会を運営・参加するための2つのWebアプリ**を1つのGitリポジトリ（モノレポ）で管理します。

| プロジェクト名 | 役割 | URL例 |
|---|---|---|
| `organizer-app` | 主催者向け（大会作成・参加者管理など） | http://localhost:8080/organizer-app/ |
| `player-app` | 参加者向け（大会参加・結果確認など） | http://localhost:8080/player-app/ |

---

## 🧑‍💻 開発環境
| 項目 | 使用ツール / バージョン |
|---|---|
| IDE | Eclipse（Enterprise Edition 推奨） |
| Java | JDK 8 |
| サーバー | Apache Tomcat 8 |
| DB | MySQL 8（予定） |
| テンプレート | JSP |
| 構成 | DAOパターン（MVC意識） |

---

## ⚙️ セットアップ手順（チームメンバー向け）

### ① 招待の承認
- GitHubでリポジトリ招待を承認。

### ② リポジトリを複製（Eclipse）
- **[ファイル] → [新規] → [Git リポジトリーの複製]**
  - **URI**：`https://github.com/K-Takahashiiii/workspace_cardgame.git`
  - **認証**：GitHubユーザー名 + 個人アクセストークン（PAT）
  - **ディレクトリ**：例 `C:\Users\<ユーザー>\git\workspace_cardgame`

> 🔐 トークンは各自で発行。共有しないこと。

### ③ プロジェクトの登録（Import）
- **[ファイル] → [インポート] → [Existing Projects into Workspace]**
  - ルート：`C:\Users\<ユーザー>\git\workspace_cardgame`
  - `organizer-app` と `player-app` を選んで **完了**

### ④ サーバー設定（初回）
- **Servers ビュー**で Tomcat v8.0 を追加
- `organizer-app` / `player-app` をドラッグで割当
- 起動確認：  
  - `http://localhost:8080/player-app/test.jsp`  
  - `http://localhost:8080/organizer-app/test.jsp`

### ⑤ コミット & プッシュ
- **Git Staging** に変更が両プロジェクト分まとめて出る
- メッセージ入力 → **Commit and Push**（一括で反映）

---

## 📁 フォルダ構成
workspace_cardgame/
├─ .git/
├─ .gitignore
├─ README.md
├─ organizer-app/
│ ├─ src/
│ └─ WebContent/
│ ├─ index.jsp
│ └─ WEB-INF/web.xml
└─ player-app/
├─ src/
└─ WebContent/
├─ index.jsp
└─ WEB-INF/web.xml

---

## ✅ 動作確認URL
| アプリ | ローカルURL例 |
|---|---|
| 参加者向け | `http://localhost:8080/player-app/test.jsp` |
| 主催者向け | `http://localhost:8080/organizer-app/test.jsp` |

---

## 💬 コミットメッセージ例
add: player-app の index.jsp を追加
fix: organizer の文字化け対策（UTF-8）
update: README にセットアップ手順を追記

---

## ⚠️ 注意
- `.git` は親ディレクトリに **1つだけ**（モノレポ）。  
- **空フォルダはGitに載らない**（ファイルを1つ以上作成してからコミット）。  
- ポート競合時は片方のTomcatを停止してから起動。

---

## 🧱 今後の開発予定（MVP）
- DAOでDB接続（MySQL Connector/J を `WEB-INF/lib` へ）
- 主催者：イベント作成・参加者一覧
- 参加者：大会一覧・エントリー
- JSP + Servlet（MVC）で画面連携
