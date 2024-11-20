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
    submitBtn.classList.add("btn", "btn-outline-warning");
    submitBtn.innerText = "Submit";

    // 새로 만든 submitBtn을 추가하고 기존의 modifyBtn과 deleteBtn을 삭제한다.
    document.getElementById('modifyForm').appendChild(submitBtn);
    document.getElementById('modifyBtn').remove();
    document.getElementById('deleteBtn').remove();
})