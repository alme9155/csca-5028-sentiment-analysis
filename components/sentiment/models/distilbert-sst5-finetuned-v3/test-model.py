#!/usr/bin/env python3
# test-model.py — Verify that your HF model folder works perfectly

from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

# Point this to your folder containing model.safetensors, config.json, tokenizer.json, etc.
model_dir = "./"   # adjust if needed

print(f"Loading model from folder: {model_dir}")
tokenizer = AutoTokenizer.from_pretrained(model_dir)
model = AutoModelForSequenceClassification.from_pretrained(model_dir)
model.eval()
print("Model loaded successfully!\n")

texts = [
    "This movie was absolutely fantastic! I loved every second.",
    "Terrible film. Worst acting I've ever seen.",
    "It was okay. Not great not bad.",
    "Incredible cinematography and powerful story!",
    "Boring and predictable. Fell asleep halfway."
]

labels = ["very negative", "negative", "neutral", "positive", "very positive"]

print("Predictions:\n" + "="*80)
with torch.no_grad():
    for text in texts:
        inputs = tokenizer(
            text,
            return_tensors="pt",
            truncation=True,
            padding=True,
            max_length=128
        )
        logits = model(**inputs).logits
        probs = torch.softmax(logits, dim=-1).squeeze().tolist()

        pred_id = probs.index(max(probs))
        confidence = max(probs)

        print(f"Text: {text}")
        print(f"Prediction: {labels[pred_id]} (id={pred_id}) — {confidence:.1%} confident")
        for label, prob in zip(labels, probs):
            print(f"   {label:15}: {prob:6.1%}")
        print("-" * 80)
