; TokiNote Windows 安装包（Inno Setup 6）。
; 由 CI 调用：choco install innosetup 后用 ISCC 编译，例如
;   ISCC.exe /DPublishDir=<publish 目录> /DAppVersion=0.3.8 /DOutputBaseName=TokiNote-0.3.8 /O"<输出目录>" windows/installer/tokinote.iss
; PublishDir 是 dotnet publish 的输出目录（含 TokiNote.exe 及其全部依赖，
; 自包含发布，不需要用户另装 .NET 运行时）。
; 本地直接编译时用下面 #ifndef 的默认值（需要先在 windows 下跑一次
; dotnet publish，把输出拷到 windows/installer/publish）。

#ifndef PublishDir
#define PublishDir "publish"
#endif
#ifndef AppVersion
#define AppVersion "0.3.8"
#endif
#ifndef OutputBaseName
#define OutputBaseName "TokiNote-0.3.8"
#endif

[Setup]
AppId=com.ichiyotsu.tokinote
AppName=TokiNote
AppVersion={#AppVersion}
AppPublisher=ichiyotsu
DefaultDirName={autopf}\TokiNote
DisableDirPage=no
DisableProgramGroupPage=yes
OutputBaseFilename={#OutputBaseName}
Compression=lzma2
SolidCompression=yes
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64
PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=dialog
UninstallDisplayIcon={app}\TokiNote.exe
SetupIconFile=..\src\TokiNote\Assets\AppIcon.ico

[Files]
Source: "{#PublishDir}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autoprograms}\TokiNote"; Filename: "{app}\TokiNote.exe"

[Run]
Filename: "{app}\TokiNote.exe"; Description: "启动 TokiNote"; Flags: nowait postinstall skipifsilent
