# 贡献指南 (Contributing Guide)

感谢你对本项目的关注！我非常欢迎大家提出各种需求。

Thank you for your interest in this project! Suggestions and feature requests are always welcome.

## 行为准则 (Code of Conduct)

由于该项目属于个人开发项目，目的是为了共同学习技术，但非常欢迎大家提 `Issue`，我将认真查看并尽力回复。

This project is maintained as a personal development project with the goal of learning and sharing technology. Everyone is welcome to open an `Issue`, and I will carefully review and respond to it.

如果有需要在项目中增加的功能，请提出 `Issue`，我将尽量实现。

If you have a feature you'd like to see added to the project, please open an `Issue`. I will do my best to implement it.

## 如何报告 Bug (How to Report a Bug)

1. 在 `Issues` 页面点击 "New Issue"。

   Click "New Issue" on the `Issues` page.

2. 选择 `Bug Report` 模板。

   Select the `Bug Report` template.

3. 填写以下信息：

   Provide the following information:

   * 复现步骤
   
     Steps to reproduce

   * 期望行为
   
     Expected behavior

   * 实际行为
   
     Actual behavior

   * 环境信息（操作系统、Java 版本等）
   
     Environment information (operating system, Java version, etc.)

## 如何提出新功能建议 (How to Suggest a New Feature)

1. 先在 `Issues` 中搜索是否有类似提议。

   First, search the existing `Issues` to see if a similar proposal already exists.

2. 如果没有，创建一个新的 Issue 并添加 `enhancement` 标签。

   If there is no similar Issue, create a new one and add the `enhancement` label.

3. 描述功能的使用场景和预期效果。

   Describe the use case and expected outcome of the proposed feature.

## 如何提交代码 (How to Submit Code)

非常抱歉，目前暂不能提供代码提交权限。

Sorry, code contribution access is currently not available.

如果你希望贡献代码，可以先通过 `Issue` 进行讨论。

If you would like to contribute code, please discuss the proposed changes through an `Issue` first.

## 开发环境搭建 (Development Environment Setup)

1. 准备 Java 环境：`JDK 25` / `OpenJDK 25`。

   Prepare a Java environment: `JDK 25` / `OpenJDK 25`.

2. 准备开发工具：`IntelliJ IDEA` / `Eclipse` / `VS Code`。

   Prepare a development IDE: `IntelliJ IDEA` / `Eclipse` / `VS Code`.

3. 准备 `Gradle` 环境。部分 IDE 已经内置 `Gradle` 支持，如果需要自定义维护，可以自行配置 `Gradle` 环境。

   Prepare a `Gradle` environment. Some IDEs provide built-in `Gradle` support. If you need a custom `Gradle` setup, you can configure it manually.

4. 拉取代码。

   Clone the repository.

    ```bash
    git clone https://github.com/KeepYoungerMap/mao-app
    cd mao-app
    ./gradlew build
    ```

    > 注意：这里使用的是项目自带的 Gradle Wrapper，因此推荐使用 `./gradlew`，而不是 `.gradle build`。
    > 
    > Note: This project uses the Gradle Wrapper, so `./gradlew` is recommended instead of `.gradle build`.

5. 使用 IDE 打开项目，并等待 `Gradle` 完成项目同步。

   Open the project with your IDE and wait for `Gradle` to finish syncing the project.
