(() => {
  const panel = document.querySelector('[data-chat-panel]');
  const messages = document.querySelector('[data-chat-messages]');
  const form = document.querySelector('[data-chat-form]');

  function openChat() {
    if (panel) panel.hidden = false;
  }

  function closeChat() {
    if (panel) panel.hidden = true;
  }

  document.querySelectorAll('[data-open-chat]').forEach(button => button.addEventListener('click', openChat));
  document.querySelectorAll('[data-close-chat]').forEach(button => button.addEventListener('click', closeChat));

  function addMessage(text, type) {
    if (!messages) return;
    const node = document.createElement('div');
    node.className = 'chat-message ' + type;
    node.textContent = text;
    messages.appendChild(node);
    messages.scrollTop = messages.scrollHeight;
  }

  function addRecommendations(items) {
    if (!messages) return;
    if (!items || items.length === 0) {
      addMessage('Jeg fandt ikke et tydeligt match. Prøv fx at nævne herre/kvinde/unisex, sommer, date night eller en duftnote.', 'bot');
      return;
    }

    const wrapper = document.createElement('div');
    wrapper.className = 'chat-message bot';

    const intro = document.createElement('div');
    intro.textContent = 'Jeg ville starte med:';
    wrapper.appendChild(intro);

    items.forEach(item => {
      const link = document.createElement('a');
      link.href = '/perfumer/' + item.slug;
      link.textContent = item.name + (item.brand ? ' — ' + item.brand : '');
      link.style.display = 'block';
      link.style.marginTop = '8px';
      wrapper.appendChild(link);

      if (item.reason) {
        const reason = document.createElement('small');
        reason.textContent = item.reason;
        reason.style.display = 'block';
        reason.style.opacity = '.72';
        wrapper.appendChild(reason);
      }
    });

    messages.appendChild(wrapper);
    messages.scrollTop = messages.scrollHeight;
  }

  async function ask(query) {
    if (!query || !query.trim()) return;
    addMessage(query.trim(), 'user');
    addMessage('Søger i kollektionen…', 'bot');
    const loading = messages ? messages.lastElementChild : null;

    try {
      const response = await fetch('/api/chatbot?q=' + encodeURIComponent(query.trim()));
      if (!response.ok) throw new Error('Request failed');
      const data = await response.json();
      if (loading) loading.remove();
      addRecommendations(data);
    } catch (error) {
      if (loading) loading.remove();
      addMessage('Jeg kunne ikke hente anbefalinger lige nu. Prøv igen om lidt.', 'bot');
    }
  }

  if (form) {
    form.addEventListener('submit', event => {
      event.preventDefault();
      const input = form.querySelector('input[name="q"]');
      const query = input ? input.value : '';
      if (input) input.value = '';
      ask(query);
    });
  }

  document.querySelectorAll('[data-prompt]').forEach(button => {
    button.addEventListener('click', () => ask(button.dataset.prompt || ''));
  });
})();
