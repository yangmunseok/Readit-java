let start, end, max;

function render() {
  const recommendList = document.querySelectorAll(".recommendation")
  recommendList.forEach((el, idx) => {
    if (idx >= start && idx <= end) {
      el.style.display = "block";
    } else {
      el.style.display = "none";
    }
  })
}

function init() {
  const recommendList = document.querySelectorAll(".recommendation");
  start = 0;
  max = recommendList.length;
  end = (max > 6) ? 6 : max;
}

function prevPage() {
  if (start === 0) {
    return;
  }
  start--;
  end--;
  render();
}

function nextPage() {
  if (end === max - 1) {
    return;
  }
  start++;
  end++;
  render();
}