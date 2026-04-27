function logout() {
  const csrf_header = document.querySelector(
      'meta[name="csrf-header"]').content;
  const csrf_token = document.querySelector('meta[name="csrf-token"]').content;
  fetch("/logout", {
    method: "POST",
    headers: {
      [csrf_header]: csrf_token,
      "Content-Type": "application/json"
    }
  }).then(() => location.reload())
}