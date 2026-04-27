let start, end, max, step;

function init(currentPage) {
  const pagination = document.querySelectorAll(".pagination");
  currentPage = currentPage - currentPage % 10
  start = currentPage / 10 * 10;
  max = pagination.length
  end = currentPage / 10 * 10 + 9;
  step = 10;
}

function render() {
  const pagination = document.querySelectorAll(".pagination");
  pagination.forEach((el, idx) => {
    if (idx + 1 >= start && idx + 1 <= end) {
      el.style.display = "block";
    } else {
      el.style.display = "none";
    }
  });
}

function handleBackward() {
  if (start === 0) {
    return;
  }
  start -= step;
  end -= step;
  render();
}

function handleForward() {
  if (end >= max) {
    return;
  }
  start += step;
  end += step;
  render();
}