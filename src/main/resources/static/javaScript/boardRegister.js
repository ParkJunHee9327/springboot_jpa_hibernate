console.log("boardRegister.js successfully recognized.");

document.getElementById('fileUploadBtn').addEventListener('click', () => {
    // fileUploadBtn을 클릭하면 id가 file인 요소가 클릭된 효과를 줌.
    document.getElementById('file').click();
})

// 실행 파일(보안 이슈로 막음)과 20MB 이상인 파일을 차단한다.
// 실행 파일인지 판단하는 정규표현식. 뒤의 RegExp는 정규표현식을 도와주는 내장 객체다!
const fileRegex = new RegExp("\.(exe|sh|bat|jar|dll|msi)$");
const maxSize = 1024*1024*20; // 20MB를 나타낸다.

function fileValidation(fileName, fileSize) {
    // 실행 파일이거나 20MB 이상이면 0을 반환한다.
    if(fileRegex.test(fileName)) {
        return 0;
    } else if (fileSize > maxSize) {
        return 0;
    } else {
        return 1;
    }
}

// 값이 변하는(change) 객체에 이벤트를 부여한다.
document.addEventListener('change', (e) => {
    if(e.target.id == 'file') {
        const fileObject = document.getElementById('file').files;
        console.log(fileObject);

        // 한 번 이라도 파일 업로드를 시도할 때 57번 코드 줄에 걸려서 disabled가 true가 되면 혼자서 되돌아올 수 없다.
        // 파일 업로드 전에 disabled를 풀어준다.
        document.getElementById('registerBtn').disabled = false;

        // 업로드할 파일을 띄워줄 div를 가져왔다.
        const fileZone = document.getElementById('fileZone');
        console.log(fileZone);
        // 이전에 추가한 파일은 삭제한다. register에서 이전에 들어온 파일은 볼 필요가 없다.
        fileZone.innerHTML = "";
        // 부트스트랩에서 가져온 list-group을 띄울 것이다.
        let ul = `<ul class="list-group list-group-flush">`;
        // 여러 파일에 대한 값을 확인하기 위해 1 * fileValidation을 해본다.
        // 하나라도 0이 나오면 isOk *= valid가 0이 되겠지?
        let isOk = 1;
        for(let file of fileObject) {
            let valid = fileValidation(file.name, file.size);
            isOk *= valid;
            ul += `<li class="list-group-item">`;
            ul += `<div class="ms-2 me-auto">`;
            ul += `${valid ? '<div class="fw-bold">업로드 가능</div>' : '<div class="fw-bold text-danger">업로드 불가능</div>'}`;
            ul += `${file.name}</div>`; // 2번째 ul +=의 div를 닫았다.
            ul += `<span class="badge text-bg-${valid ? 'success' : 'danger'} rounded-pill>${file.size}</span>"></li>`; // 맨 위 <li>를 닫았다.
        }

        ul += `</ul>`; // <li>로 파일 개수만큼 다 담았다면 ul을 닫는다.
        fileZone.innerHTML = ul; // 누적한 요소들을 id="fileZone"인 div에 HTML로 넣는다.

        if(isOk == 0) { // 유효성 검증에 실패하면 등록 버튼을 비활성화한다.
            document.getElementById('registerBtn').disabled = false;
        }
    }
});