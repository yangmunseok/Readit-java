let showMenu = false;
let isEditing = false;
let score = 0;

function switchMenuState(id) {
  console.log("switchMenuState invoked");
  const menu = document.querySelector(".comment-menu-" + id);
  let showMenu = menu.classList.contains("hidden");
  if (showMenu) {
    menu.classList.add("flex");
    menu.classList.add("flex-col");
    menu.classList.remove("hidden");
  } else {
    menu.classList.add("hidden");
    menu.classList.remove("flex");
    menu.classList.remove("flex-col");
  }

}

function startEditingMode(id) {
  let editingMenu = document.querySelector(
      ".editing-menu-" + id);
  let defaultMenu = document.querySelector(
      ".default-menu-" + id);
  editingMenu.classList.remove("hidden");
  defaultMenu.classList.add("hidden");
}

function renderEditScore(id, score) {
  console.log("renderScore invoked")
  let labels = document.querySelectorAll(".edit-score-" + id);
  labels.forEach((el, idx) => {
    if (idx < score) {
      el.classList.add("fa-solid");
      el.classList.remove("fa-regular");
      return;
    }
    el.classList.add("fa-regular");
    el.classList.remove("fa-solid");
  })
}

function deleteReview(id, csrf_header, csrf_token) {
  console.log("deleteReview invoked()")
  console.log(csrf_header)

  fetch("/comments",
      {
        method: "DELETE",
        headers: {
          [csrf_header]: csrf_token,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({id})
      }).then(() => document.querySelector("#comment-box-" + id).remove());
}

function likeOrUnlikeReview(id, csrf_header, csrf_token) {

  fetch(`/comments/${id}/likeOrUnlike`, {
    method: "POST",
    headers: {
      [csrf_header]: csrf_token,
      "Content-Type": "application/json"
    }
  }).then(async (body) => {
    const html = await body.text();
    document.querySelector("#comment-box-" + id).outerHTML = html;
  });
}