console.log("boardDetail.js recognized.");

// listBtn을 누르면 list 페이지로 이동한다.
document.getElementById('listBtn').addEventListener('click', () => {
    location.href="/board/list";
});


document.getElementById('modifyBtn').addEventListener('click', () => {
    // modifyBtn을 누르면 title, content의 readonly를 해제한다.
    document.getElementById('title').readOnly = false;
    document.getElementById('content').readOnly = false;

    // 변경 사항을 보낼 수 있도록 제출 버튼을 생성한다.
    let submitBtn = document.createElement("button");
    submitBtn.setAttribute("type", "submit");
    submitBtn.setAttribute("id", "registerBtn");
    submitBtn.classList.add("btn", "btn-outline-warning");
    submitBtn.innerText = "Submit";

    // 새로 만든 submitBtn을 추가하고 기존의 modifyBtn과 deleteBtn을 삭제한다.
    document.getElementById('modifyForm').appendChild(submitBtn);
    document.getElementById('modifyBtn').remove();
    document.getElementById('deleteBtn').remove();

    // 파일 영역이 추가되어 modifyBtn이 눌렸을 때 파일이 변화하도록 만들었다.
    // modifyBtn을 누르면 file-del-btn들의 disabled가 풀린다.
    let fileDelBtn = document.querySelectorAll('.file-del-btn');
    for(let delBtn of fileDelBtn) {
        delBtn.disabled = false;
    }

    // 파일 업로드 버튼의 disabled를 해제한다.
    document.getElementById('fileUploadBtn').disabled = false;
})

document.addEventListener('click', (e) => {
    if(e.target.classList.contains('file-del-btn')) {
        let uuid = e.target.dataset.uuid;
        fileRemoveToServer(uuid).then(result => {
            if(result > 0) {
                alert("File successfully deleted.");
                e.target.closest('li').remove();
            }
        })
    }
})

// 비동기로 데이터를 보낸다.
async function fileRemoveToServer(uuid) {
    console.log(uuid);
    try {
        const url = "/board/fileRemove/" + uuid;
        const config = {
            method: "delete"
        }
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch(error) {
        console.log(error);
    }
}