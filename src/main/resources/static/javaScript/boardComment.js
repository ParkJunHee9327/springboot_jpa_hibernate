
document.getElementById('commentAddBtn').addEventListener('click', () => {
    const commentText = document.getElementById('commentText');
    const commentWriter = document.getElementById('commentWriter');

    if(commentText.value == "" || commentText.value == null) {
        alert("Please type comment first.");
        commentText.focus();
        return false;
    }

    let commentData = {
        bno: bnoVal,
        writer: commentWriter.innerText,
        content: commentText.value,
    }

    storeCommentInServer(commentData).then(result => {
        if(result == '1') { // 여기의 result는 CommentController 내 insertComment의 return값에 따라 다르다.
            alert("Comment successfully stored.");
            commentText.value = "";
            // displayComment(bnoVal);
        }
    });
});

async function storeCommentInServer(commentData) {
    try {
        const url = "/comment/insert";
        const config = {
            method: "post",
            headers : {
                'Content-Type' : 'application/json; charset=utf-8'
            },
            body: JSON.stringify(commentData)
        };

        const response = await fetch(url, config);
        const result = await response.text();
        return result;
    } catch(error) {
        console.log(error);
    }
}