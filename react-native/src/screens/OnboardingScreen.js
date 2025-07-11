import React, { useState } from 'react';
import { View, TextInput, Button, StyleSheet } from 'react-native';
import { doc, setDoc } from 'firebase/firestore';
import { db, auth } from '../firebase/config';

export default function OnboardingScreen({ navigation }) {
  const [goal, setGoal] = useState('');
  const [level, setLevel] = useState('');

  const handleSave = async () => {
    const uid = auth.currentUser.uid;
    await setDoc(doc(db, 'users', uid), {
      goal,
      level
    });
    navigation.replace('Dashboard');
  };

  return (
    <View style={styles.container}>
      <TextInput placeholder="Fitness Goal" value={goal} onChangeText={setGoal} style={styles.input}/>
      <TextInput placeholder="Fitness Level" value={level} onChangeText={setLevel} style={styles.input}/>
      <Button title="Save" onPress={handleSave} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  input: { borderWidth: 1, borderColor: '#ccc', padding: 8, marginBottom: 10 }
});
