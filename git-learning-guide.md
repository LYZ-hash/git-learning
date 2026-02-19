# Git & GitHub 企业级实战学习指南

## 一、项目简介

### 为什么选这个项目？

我们将从零构建一个 **TaskFlow — 轻量级任务管理平台**（类似简化版 Jira/Trello）。

选择它的理由：

| 维度 | 说明 |
|---|---|
| **多模块** | 用户、项目、任务、评论等模块天然可拆分为多个 Feature 分支 |
| **多版本** | v1.0 基础 CRUD → v1.1 认证鉴权 → v2.0 新功能，完美模拟版本迭代 |
| **可模拟多人协作** | 你一个人也可以扮演不同角色（开发者 A / B / Code Reviewer），练习冲突解决和 PR 流程 |
| **技术栈贴合** | Spring Boot + 简单前端页面，适合 Java 后端工程师 |
| **复杂度适中** | 项目本身不难，让你把精力集中在 Git 操作上 |

### 技术栈

- **后端**：Java 17 + Spring Boot 3 + Spring Data JPA + H2（开发）/ MySQL（生产）
- **前端**：简单的 HTML + JS 页面（或 Thymeleaf 模板），够用即可
- **构建**：Maven
- **CI/CD**：GitHub Actions
- **代码托管**：GitHub

### 功能概览

| 版本 | 核心功能 |
|---|---|
| v1.0.0 | 用户注册/登录（无鉴权）、项目 CRUD、任务 CRUD |
| v1.1.0 | JWT 认证鉴权、任务分配、任务状态流转 |
| v1.2.0 | 评论系统、任务优先级与标签 |
| v2.0.0 | 仪表盘统计、导出功能、全局搜索 |

---

## 二、Git 工作流选型

我们采用 **GitHub Flow + 版本标签** 的混合模式，这是目前互联网企业中最主流的工作流之一。

```
main ────●────●────●────●──── (始终可部署)
          \        / \      /
           feature/   hotfix/
```

### 分支命名规范

| 类型 | 格式 | 示例 |
|---|---|---|
| 主分支 | `main` | `main` |
| 功能分支 | `feature/<模块>-<描述>` | `feature/user-registration` |
| 修复分支 | `fix/<issue号>-<描述>` | `fix/42-login-null-pointer` |
| 热修复分支 | `hotfix/<版本>-<描述>` | `hotfix/v1.0.1-sql-injection` |
| 发布分支 | `release/<版本号>` | `release/v1.1.0` |

### Commit Message 规范（Conventional Commits）

```
<type>(<scope>): <subject>

<body>       ← 可选
<footer>     ← 可选（如 BREAKING CHANGE / Closes #123）
```

| type | 含义 |
|---|---|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档变更 |
| `style` | 代码格式（不影响逻辑） |
| `refactor` | 重构（不增功能、不修 Bug） |
| `test` | 测试相关 |
| `chore` | 构建/工具链/依赖变更 |
| `ci` | CI/CD 配置变更 |

示例：
```
feat(user): 新增用户注册接口

- 添加 UserController.register()
- 添加 DTO 入参校验
- 添加单元测试

Closes #5
```

---

## 三、分阶段学习计划

下面将整个项目拆成 **7 个阶段**，每个阶段聚焦一组 Git 操作。每阶段给出：要做什么（项目任务）、会学到什么（Git 技能）、以及具体怎么做（操作步骤）。

---

### 阶段 1：仓库初始化与基本配置

> **项目任务**：创建 Spring Boot 项目骨架，初始化 Git 仓库，推送到 GitHub。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 初始化本地仓库 | `git init` |
| 关联远程仓库 | `git remote add origin <url>` |
| 查看远程信息 | `git remote -v` |
| `.gitignore` 配置 | 排除 `target/`、`.idea/`、`*.class` 等 |
| 首次提交 | `git add .` → `git commit -m "chore: init project"` |
| 推送到远程 | `git push -u origin main` |
| 配置用户信息 | `git config user.name` / `git config user.email` |
| 查看仓库状态 | `git status`、`git log --oneline --graph` |

#### 具体步骤

```bash
# 1. 创建项目（用 Spring Initializr 或 CLI）
# 2. 进入项目目录
git init
git remote add origin git@github.com:<你的用户名>/taskflow.git

# 3. 创建 .gitignore
cat > .gitignore << 'EOF'
target/
.idea/
*.iml
*.class
.DS_Store
*.log
application-local.yml
EOF

# 4. 首次提交
git add .
git commit -m "chore: initialize Spring Boot project skeleton"
git push -u origin main

# 5. 保护 main 分支（GitHub 网页端）
#    Settings → Branches → Add rule → main
#    ✅ Require pull request reviews before merging
#    ✅ Require status checks to pass before merging
```

---

### 阶段 2：Feature 分支开发与 PR 流程

> **项目任务**：开发用户模块（Entity、Repository、Service、Controller），通过 PR 合入 main。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 创建并切换分支 | `git switch -c feature/user-module` |
| 分阶段暂存 | `git add -p`（交互式选择要暂存的代码块） |
| 规范提交 | `git commit -m "feat(user): ..."` |
| 推送功能分支 | `git push origin feature/user-module` |
| 发起 Pull Request | GitHub 网页端操作 |
| Code Review | 在 PR 页面评论、Request Changes、Approve |
| PR 合并策略 | Squash and Merge / Rebase and Merge / Create a Merge Commit |
| 删除远程分支 | PR 合并后自动删除或 `git push origin --delete feature/user-module` |
| 删除本地分支 | `git branch -d feature/user-module` |
| 同步主干 | `git switch main` → `git pull origin main` |

#### 具体步骤

```bash
# 1. 创建功能分支
git switch main
git pull origin main
git switch -c feature/user-module

# 2. 开发 User 模块（Entity → Repository → Service → Controller）
#    每完成一个层次做一次有意义的提交
git add src/main/java/.../entity/User.java
git commit -m "feat(user): add User entity with JPA annotations"

git add src/main/java/.../repository/UserRepository.java
git commit -m "feat(user): add UserRepository interface"

# ... 继续 Service、Controller

# 3. 推送到远程
git push origin feature/user-module

# 4. 在 GitHub 网页端发起 PR
#    - 填写标题：feat(user): implement user module
#    - 填写描述：说明做了什么、如何测试
#    - 指定 Reviewer（模拟场景下可以自己审查）
#    - 关联 Issue（如果有的话）

# 5. Review 通过后，选择 "Squash and Merge"
# 6. 清理
git switch main
git pull origin main
git branch -d feature/user-module
```

#### 🔑 重点练习：`git add -p`（交互式暂存）

这是一个很多人忽略但极其实用的操作。当你在一个文件里同时做了两件事（如既修了 Bug 又加了新功能），可以用 `git add -p` 只暂存其中一部分修改，做到「一个 commit 只做一件事」。

```bash
git add -p src/main/java/.../UserService.java
# Git 会逐块展示差异，你可以选择：
# y = 暂存这块  n = 跳过  s = 拆分成更小的块  e = 手动编辑
```

---

### 阶段 3：多分支并行与冲突解决

> **项目任务**：同时开发「项目模块」和「任务模块」，模拟两个开发者并行开发，制造并解决合并冲突。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 并行分支 | 同时维护 `feature/project-module` 和 `feature/task-module` |
| 与主干同步（变基） | `git fetch origin` → `git rebase origin/main` |
| 合并冲突解决 | 编辑冲突文件 → `git add` → `git rebase --continue` |
| 放弃变基 | `git rebase --abort` |
| 合并（非快进） | `git merge --no-ff feature-branch` |
| 放弃合并 | `git merge --abort` |
| 临时保存工作区 | `git stash` / `git stash pop` / `git stash list` |
| 查看差异 | `git diff`、`git diff --staged` |

#### 具体步骤

```bash
# === 模拟开发者 A：开发项目模块 ===
git switch -c feature/project-module
# 编写 Project 实体、仓库、服务、控制器
# 关键：在 application.yml 中添加一些配置
git add . && git commit -m "feat(project): implement project CRUD"
git push origin feature/project-module

# === 模拟开发者 B：开发任务模块 ===
git switch main
git switch -c feature/task-module
# 编写 Task 实体（与 Project 有关联关系）
# 关键：也修改 application.yml 中的同一区域 ← 这就会产生冲突！
git add . && git commit -m "feat(task): implement task CRUD"
git push origin feature/task-module

# === 先合入 project-module 的 PR ===
# （在 GitHub 上操作）

# === 再处理 task-module 的 PR ===
# 此时 task-module 与 main 有冲突
git switch feature/task-module
git fetch origin
git rebase origin/main
# ⚠️ 出现冲突！
# 打开冲突文件，找到 <<<<<<< / ======= / >>>>>>> 标记
# 手动编辑，解决冲突
git add <conflicted-file>
git rebase --continue
git push --force-with-lease origin feature/task-module
# 注意：这里用 --force-with-lease 而非 --force，更安全
```

#### 🔑 重点练习：`git stash`

```bash
# 你正在 feature/task-module 上开发，写到一半
# 突然被通知 feature/project-module 的 PR 有问题需要你看
git stash save "task module WIP: service layer half done"
git switch feature/project-module
# ... 处理完问题
git switch feature/task-module
git stash pop
# 继续之前的工作
```

#### 🔑 重点练习：`--force-with-lease` vs `--force`

```bash
# ❌ 危险：git push --force  → 会覆盖远程所有内容，如果别人也推过代码会丢失
# ✅ 安全：git push --force-with-lease → 只有在远程没有新提交时才会强推，否则失败
```

---

### 阶段 4：提交历史整理与交互式变基

> **项目任务**：为任务模块添加「状态流转」功能，期间刻意制造零碎提交，然后通过交互式变基整理历史。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 交互式变基 | `git rebase -i HEAD~N` |
| 压缩提交 (Squash) | 在变基编辑器中将 `pick` 改为 `squash` 或 `s` |
| 重新排序提交 | 在变基编辑器中调整行顺序 |
| 修改历史 Commit Message | 在变基编辑器中将 `pick` 改为 `reword` 或 `r` |
| 删除某个提交 | 在变基编辑器中将 `pick` 改为 `drop` 或 `d` |
| 追加到上次提交 | `git commit --amend` |
| 查看提交历史 | `git log --oneline --graph --all` |

#### 具体步骤

```bash
git switch -c feature/task-status

# 故意制造零碎提交来练习
git commit -m "feat(task): add status enum"
git commit -m "fix typo"
git commit -m "wip"
git commit -m "feat(task): add status transition logic"
git commit -m "oops forgot import"
git commit -m "test: add unit test for status transition"

# 现在有 6 个乱七八糟的提交，PR 前整理它们
git rebase -i HEAD~6

# 编辑器会打开，显示如下：
# pick abc1234 feat(task): add status enum
# pick def5678 fix typo
# pick ghi9012 wip
# pick jkl3456 feat(task): add status transition logic
# pick mno7890 oops forgot import
# pick pqr1234 test: add unit test for status transition

# 改为：
# pick abc1234 feat(task): add status enum
# squash def5678 fix typo            ← 合入上一个
# squash ghi9012 wip                 ← 合入上一个
# pick jkl3456 feat(task): add status transition logic
# squash mno7890 oops forgot import  ← 合入上一个
# pick pqr1234 test: add unit test for status transition

# 保存退出后，Git 会让你编辑合并后的 Commit Message
# 最终得到 3 个干净的提交

git push origin feature/task-status
```

#### 🔑 重点练习：`git commit --amend`

```bash
# 刚提交完，发现忘记加一个文件
git add src/main/java/.../TaskStatus.java
git commit --amend --no-edit
# --no-edit: 不修改 Commit Message，仅追加文件

# 如果只是想改 Commit Message
git commit --amend -m "feat(task): add TaskStatus enum with transitions"
```

---

### 阶段 5：版本发布、标签管理与热修复

> **项目任务**：发布 v1.0.0，然后模拟发现线上 Bug，通过 hotfix 流程紧急修复并发布 v1.0.1。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 创建附注标签 | `git tag -a v1.0.0 -m "Release v1.0.0"` |
| 推送标签 | `git push origin v1.0.0` 或 `git push origin --tags` |
| 查看标签 | `git tag -l` / `git show v1.0.0` |
| 删除标签 | `git tag -d v1.0.0` / `git push origin --delete v1.0.0` |
| Hotfix 分支 | `git switch -c hotfix/v1.0.1-xss-fix` |
| Cherry-pick | `git cherry-pick <commit-hash>` |
| GitHub Release | 在网页端基于 Tag 创建 Release，编写 Release Notes |

#### 具体步骤

```bash
# === 发布 v1.0.0 ===
git switch main
git pull origin main
git tag -a v1.0.0 -m "Release v1.0.0: 基础用户、项目、任务 CRUD"
git push origin v1.0.0

# 在 GitHub 网页端：Releases → Create a new release
# 选择 tag v1.0.0，填写 Release Notes（变更列表）

# === 模拟发现线上 Bug ===
# 假设发现了一个 XSS 漏洞

# 1. 从 main 拉出 hotfix 分支
git switch -c hotfix/v1.0.1-xss-fix main
# 2. 修复 Bug
# 修改 UserController.java，添加输入校验
git add . && git commit -m "fix(security): sanitize user input to prevent XSS"
# 3. 发起 PR 到 main（紧急 Review，快速合入）
git push origin hotfix/v1.0.1-xss-fix
# 4. PR 合并后打 hotfix tag
git switch main && git pull origin main
git tag -a v1.0.1 -m "Hotfix v1.0.1: fix XSS vulnerability"
git push origin v1.0.1

# === Cherry-pick 场景 ===
# 假设你在 feature/comment-module 分支上修了一个通用的工具类 Bug
# 这个 Bug 需要立刻同步到 main 上线，但 feature 分支的其他代码还没开发完

git log --oneline  # 找到修复 Bug 的那个 commit hash, 假设是 a1b2c3d
git switch main
git cherry-pick a1b2c3d
# 如果有冲突，解决后：
git add . && git cherry-pick --continue
git push origin main
```

---

### 阶段 6：CI/CD 集成（GitHub Actions）

> **项目任务**：配置 GitHub Actions，实现提交自动构建、测试，PR 自动检查。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 创建工作流文件 | `.github/workflows/ci.yml` |
| CI 状态与 PR 关联 | GitHub 自动在 PR 页面显示 CI 状态 |
| Badge 添加 | 在 README 中添加 CI 状态徽章 |
| 分支保护规则 | 要求 CI 通过才能合并 PR |

#### 工作流配置示例

```yaml
# .github/workflows/ci.yml
name: CI Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}

      - name: Build & Test
        run: mvn clean verify

      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: target/surefire-reports/
```

#### 具体步骤

```bash
git switch -c ci/setup-github-actions
mkdir -p .github/workflows
# 创建 ci.yml（内容如上）
git add .github/
git commit -m "ci: add GitHub Actions CI pipeline"
git push origin ci/setup-github-actions
# 发起 PR，观察 CI 是否自动运行
```

---

### 阶段 7：高级排障与历史操作

> **项目任务**：在继续开发评论模块的过程中，练习各种高级排障和历史操作技巧。

#### 涉及的 Git 操作

| 操作 | 命令 / 动作 |
|---|---|
| 安全回滚 | `git revert <commit-hash>` |
| 本地重置 | `git reset --soft` / `--mixed` / `--hard` |
| 后悔药 | `git reflog` |
| 责任追溯 | `git blame <file>` |
| 二分查找 Bug | `git bisect` |
| 查看文件历史 | `git log --follow -p <file>` |
| 查看某次提交 | `git show <commit-hash>` |
| 比较分支差异 | `git diff main..feature-branch` |

#### 场景 1：线上回滚（`git revert`）

```bash
# 发现 main 上最新合入的 PR 有问题，需要紧急回滚
# 注意：绝对不要在公共分支上使用 git reset！

git switch main
git log --oneline -5  # 找到需要回滚的 commit
git revert <commit-hash>
# Git 会创建一个新的 "反向" 提交，撤销之前的更改，但保留完整历史
git push origin main
```

#### 场景 2：本地重置（`git reset`）

```bash
# 你在本地做了 3 个提交，还没 push，想全部撤回重做

# --soft: 撤销 commit，但保留暂存区和工作区的改动
git reset --soft HEAD~3

# --mixed（默认）: 撤销 commit 和暂存区，保留工作区改动
git reset HEAD~3

# --hard: 撤销一切（危险！所有改动丢失）
git reset --hard HEAD~3
```

#### 场景 3：`git reflog` — 终极后悔药

```bash
# 糟糕！刚才误操作 git reset --hard 把代码弄丢了！
git reflog
# 输出类似：
# a1b2c3d HEAD@{0}: reset: moving to HEAD~3
# d4e5f6g HEAD@{1}: commit: feat(comment): add reply feature
# h7i8j9k HEAD@{2}: commit: feat(comment): add comment service
# ...

# 找到丢失前的 commit hash，恢复
git reset --hard d4e5f6g  # 或 git switch -c recovery-branch d4e5f6g
```

#### 场景 4：`git blame` — 责任追溯

```bash
# 线上报错，某行代码写得有问题，想知道是谁什么时候改的
git blame src/main/java/.../TaskService.java

# 输出每一行代码的最后修改者、提交哈希和时间
# 可以结合行号缩小范围
git blame -L 45,60 src/main/java/.../TaskService.java
```

#### 场景 5：`git bisect` — 二分法查 Bug

```bash
# 发现一个 Bug，但不知道哪个提交引入的
git bisect start
git bisect bad                    # 当前版本有 Bug
git bisect good v1.0.0            # v1.0.0 确认没有这个 Bug

# Git 自动检出中间版本让你测试
# 测试后告诉 Git 结果：
git bisect good   # 这个版本没 Bug
# 或
git bisect bad    # 这个版本有 Bug

# Git 继续二分，直到找到引入 Bug 的精确 commit
# 结束后重置
git bisect reset
```

---

## 四、完整 Git 操作速查表

按使用频率和场景整理的所有操作：

### 📌 每日必用

| 操作 | 命令 |
|---|---|
| 查看状态 | `git status` |
| 查看历史 | `git log --oneline --graph --all` |
| 创建分支 | `git switch -c <branch-name>` |
| 切换分支 | `git switch <branch-name>` |
| 暂存文件 | `git add <file>` 或 `git add -p` |
| 提交 | `git commit -m "type(scope): message"` |
| 推送 | `git push origin <branch>` |
| 拉取（变基） | `git pull --rebase origin main` |
| 获取远程信息 | `git fetch origin` |

### 🔧 常用操作

| 操作 | 命令 |
|---|---|
| 暂存工作区 | `git stash` / `git stash pop` |
| 追加提交 | `git commit --amend` |
| 查看差异 | `git diff` / `git diff --staged` |
| 查看分支 | `git branch -a` |
| 删除本地分支 | `git branch -d <branch>` |
| 删除远程分支 | `git push origin --delete <branch>` |
| 合并（非快进） | `git merge --no-ff <branch>` |
| 打标签 | `git tag -a v1.0.0 -m "message"` |

### ⚡ 进阶操作

| 操作 | 命令 |
|---|---|
| 交互式变基 | `git rebase -i HEAD~N` |
| 安全强推 | `git push --force-with-lease` |
| Cherry-pick | `git cherry-pick <hash>` |
| 安全回滚 | `git revert <hash>` |
| 后悔药 | `git reflog` |
| 责任追溯 | `git blame <file>` |
| 二分查 Bug | `git bisect start` / `good` / `bad` |
| 本地重置 | `git reset --soft/--mixed/--hard` |
| 文件历史 | `git log --follow -p <file>` |
| 比较分支 | `git diff main..<branch>` |

---

## 五、GitHub 协作操作清单

除了 Git 命令行，以下 GitHub 网页端操作也是企业开发中的必备技能：

| 操作 | 说明 |
|---|---|
| **创建 Repository** | 选 Public/Private，添加 README、`.gitignore`、License |
| **Issue 管理** | 创建 Issue，添加 Label（bug/feature/enhancement），指定 Assignee，关联 Milestone |
| **Pull Request** | 发起 PR、填写描述、关联 Issue（`Closes #12`）、指定 Reviewer |
| **Code Review** | Comment / Request Changes / Approve |
| **PR 合并策略** | Merge Commit / Squash and Merge / Rebase and Merge（理解三者区别） |
| **Branch Protection Rules** | 保护 main 分支：要求 PR Review、CI 通过、禁止 Force Push |
| **GitHub Actions** | 配置 CI/CD 工作流 |
| **Releases** | 基于 Tag 创建 Release，编写 Release Notes |
| **Projects (看板)** | 用 GitHub Projects 管理任务进度（模拟 Sprint） |
| **Wiki** | 编写项目文档（API 文档、开发指南） |
| **Discussions** | 团队技术讨论 |

---

## 六、学习路线总结

```mermaid
graph LR
    A[阶段1<br>仓库初始化] --> B[阶段2<br>分支开发与PR]
    B --> C[阶段3<br>多分支并行<br>冲突解决]
    C --> D[阶段4<br>历史整理<br>交互式变基]
    D --> E[阶段5<br>版本发布<br>标签与热修复]
    E --> F[阶段6<br>CI/CD集成]
    F --> G[阶段7<br>高级排障<br>与历史操作]

    style A fill:#4CAF50,color:#fff
    style B fill:#2196F3,color:#fff
    style C fill:#FF9800,color:#fff
    style D fill:#9C27B0,color:#fff
    style E fill:#F44336,color:#fff
    style F fill:#00BCD4,color:#fff
    style G fill:#795548,color:#fff
```

### ⚠️ 注意事项

1. **先学会后再用**：每个阶段先在测试分支上练习 Git 操作，熟练后再在正式开发中使用
2. **养成好习惯**：从一开始就遵循 Conventional Commits，写有意义的 Commit Message
3. **不要怕犯错**：Git 的设计哲学就是「几乎一切都可以撤销」，大胆尝试
4. **理解原理**：不只是记命令，要理解 Git 的三个区域（工作区、暂存区、仓库）和 HEAD 指针的概念
5. **模拟多人协作**：一个人可以通过多个分支、多个 GitHub 账号，或同一账号不同身份来模拟

### 🎯 学完之后你将掌握

- ✅ 从零搭建一个规范的 GitHub 项目
- ✅ 熟练使用 Feature Branch 工作流
- ✅ 处理合并冲突而不慌张
- ✅ 通过 PR + Code Review 进行团队协作
- ✅ 整理提交历史，保持 Git Log 整洁
- ✅ 版本发布、标签管理、热修复的完整流程
- ✅ CI/CD 基本配置
- ✅ 线上排障的各种 Git 技巧（revert / blame / bisect / reflog）
